package io.github.binaryfoo.decoders.bit;

import io.github.binaryfoo.bit.EmvBit;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmvBitStringParser {

    private static final Pattern ENUMERATED_FIELD_PATTERN = Pattern.compile("\\s*\\((\\d+),(\\d+)\\)=(\\d+)\\s*");
    private static final Pattern NUMERIC_FIELD_PATTERN = Pattern.compile("\\s*\\((\\d+),(\\d+)-(\\d+)\\)\\s*");

    public static BitStringField parseField(String key, String label) {
        if (key.contains("-")) {
            return parseNumericField(key, label);
        } else {
            return parseEnumeratedField(key, label);
        }
    }

    public static List<BitStringField> parse(List<String> lines) throws IOException {
        List<BitStringField> bitMappings = new ArrayList<>();
        for (String line : lines) {
            if (StringUtils.isNotBlank(line) && !line.startsWith("#")) {
                String[] fields = line.split("\\s*:\\s*", 2);
                bitMappings.add(parseField(fields[0], fields[1]));
            }
        }
        return bitMappings;
    }

    private static BitStringField parseNumericField(String key, String label) {
        Matcher matcher = NUMERIC_FIELD_PATTERN.matcher(key);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Not a valid numeric field mapping [" + key + "]");
        }
        int byteNumber = Integer.parseInt(matcher.group(1));
        int firstBit = Integer.parseInt(matcher.group(2));
        int lastBit = Integer.parseInt(matcher.group(3));
        return new NumericBitStringField(byteNumber, firstBit, lastBit, label);
    }

    private static BitStringField parseEnumeratedField(String key, String label) {
        Set<EmvBit> bits = new TreeSet<>();
        for (String bit : key.split("&")) {
            bits.add(parseBit(bit));
        }
        return new EnumeratedBitStringField(bits, label);
    }

    private static EmvBit parseBit(String key) {
        Matcher matcher = ENUMERATED_FIELD_PATTERN.matcher(key);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Not a valid enumerated field mapping [" + key + "]");
        }
        int byteNumber = Integer.parseInt(matcher.group(1));
        int bitNumber = Integer.parseInt(matcher.group(2));
        boolean bitValue = matcher.group(3).equals("1");
        return new EmvBit(byteNumber, bitNumber, bitValue);
    }
}
