package io.github.binaryfoo.decoders;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.Decoder;
import io.github.binaryfoo.bit.EmvBit;
import io.github.binaryfoo.decoders.bit.BitStringField;
import io.github.binaryfoo.decoders.bit.EnumeratedBitStringField;
import io.github.binaryfoo.decoders.bit.NumericBitStringField;
import io.github.binaryfoo.io.ClasspathIO;
import io.github.binaryfoo.tlv.ISOUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmvBitStringDecoder implements Decoder {

    private static final Pattern ENUMERATED_FIELD_PATTERN = Pattern.compile("\\s*\\((\\d+),(\\d+)\\)=(\\d+)\\s*");
    private static final Pattern NUMERIC_FIELD_PATTERN = Pattern.compile("\\s*\\((\\d+),(\\d+)-(\\d+)\\)\\s*");

    private final List<BitStringField> bitMappings = new ArrayList<>();
    private final int lengthInCharacters;
    private final boolean showFieldHexInDecode;

    public EmvBitStringDecoder(String fileName, boolean showFieldHexInDecode) {
        this(ClasspathIO.open(fileName), showFieldHexInDecode);
    }

    public EmvBitStringDecoder(InputStream input, boolean showFieldHexInDecode) {
        this.showFieldHexInDecode = showFieldHexInDecode;
        try {
            for (String line : IOUtils.readLines(input)) {
                if (StringUtils.isNotBlank(line) && !line.startsWith("#")) {
                    String[] fields = line.split("\\s*:\\s*", 2);
                    bitMappings.add(parseField(fields[0], fields[1]));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(input);
        }
        this.lengthInCharacters = findMaxLengthInBytes() * 2;
    }

    private int findMaxLengthInBytes() {
        int maxLength = 0;
        for (BitStringField mapping : bitMappings) {
            maxLength = Math.max(mapping.getStartBytesOffset() + mapping.getLengthInBytes(), maxLength);
        }
        return maxLength;
    }

    private BitStringField parseField(String key, String label) {
        if (key.contains("-")) {
            return parseNumericField(key, label);
        } else {
            return parseEnumeratedField(key, label);
        }
    }

    private BitStringField parseNumericField(String key, String label) {
        Matcher matcher = NUMERIC_FIELD_PATTERN.matcher(key);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Not a valid numeric field mapping [" + key + "]");
        }
        int byteNumber = Integer.parseInt(matcher.group(1));
        int firstBit = Integer.parseInt(matcher.group(2));
        int lastBit = Integer.parseInt(matcher.group(3));
        return new NumericBitStringField(byteNumber, firstBit, lastBit, label);
    }

    private BitStringField parseEnumeratedField(String key, String label) {
        Set<EmvBit> bits = new TreeSet<>();
        for (String bit : key.split("&")) {
            bits.add(parseBit(bit));
        }
        return new EnumeratedBitStringField(bits, label);
    }

    private EmvBit parseBit(String key) {
        Matcher matcher = ENUMERATED_FIELD_PATTERN.matcher(key);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Not a valid enumerated field mapping [" + key + "]");
        }
        int byteNumber = Integer.parseInt(matcher.group(1));
        int bitNumber = Integer.parseInt(matcher.group(2));
        boolean bitValue = matcher.group(3).equals("1");
        return new EmvBit(byteNumber, bitNumber, bitValue);
    }

    @Override
    public List<DecodedData> decode(String input, int startIndexInBytes, DecodeSession decodeSession) {
        List<DecodedData> decoded = new ArrayList<>();
        Set<EmvBit> bits = EmvBit.fromHex(input);
        for (BitStringField field : bitMappings) {
            String v = field.getValueIn(bits);
            if (v != null) {
                int fieldStartIndex = startIndexInBytes + field.getStartBytesOffset();
                decoded.add(new DecodedData(field.getPositionIn(showFieldHexInDecode ? bits : null), v, fieldStartIndex, fieldStartIndex + field.getLengthInBytes()));
            }
        }
        return decoded;
    }

    @Override
    public String validate(String bitString) {
        if (bitString == null || bitString.length() != lengthInCharacters) {
            return String.format("Value must be exactly %d characters", lengthInCharacters);
        }
        if (!ISOUtil.isValidHexString(bitString)) {
            return "Value must contain only the characters 0-9 and A-F";
        }
        return null;
    }

    @Override
    public int getMaxLength() {
        return lengthInCharacters;
    }

}
