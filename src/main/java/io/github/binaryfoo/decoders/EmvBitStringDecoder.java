package io.github.binaryfoo.decoders;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.Decoder;
import io.github.binaryfoo.bit.BitPackage;
import io.github.binaryfoo.bit.EmvBit;
import io.github.binaryfoo.decoders.bit.BitStringField;
import io.github.binaryfoo.decoders.bit.EmvBitStringParser;
import io.github.binaryfoo.io.ClasspathIO;
import io.github.binaryfoo.tlv.ISOUtil;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EmvBitStringDecoder implements Decoder {

    private final List<BitStringField> bitMappings;
    private final int lengthInCharacters;
    private final boolean showFieldHexInDecode;

    public EmvBitStringDecoder(String fileName, boolean showFieldHexInDecode) {
        this(ClasspathIO.open(fileName), showFieldHexInDecode);
    }

    public EmvBitStringDecoder(InputStream input, boolean showFieldHexInDecode) {
        this.showFieldHexInDecode = showFieldHexInDecode;
        try {
            bitMappings = EmvBitStringParser.parse(IOUtils.readLines(input));
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

    @Override
    public List<DecodedData> decode(@NotNull String input, int startIndexInBytes, @NotNull DecodeSession decodeSession) {
        List<DecodedData> decoded = new ArrayList<>();
        Set<EmvBit> bits = BitPackage.fromHex(input);
        for (BitStringField field : bitMappings) {
            String v = field.getValueIn(bits);
            if (v != null) {
                int fieldStartIndex = startIndexInBytes + field.getStartBytesOffset();
                decoded.add(DecodedData.primitive(field.getPositionIn(showFieldHexInDecode ? bits : null), v, fieldStartIndex, fieldStartIndex + field.getLengthInBytes()));
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
