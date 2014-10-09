package io.github.binaryfoo.decoders;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.Decoder;
import io.github.binaryfoo.bit.EmvBit;

import java.util.ArrayList;
import java.util.List;

/**
 * Decode and label bits in a string according to the EMV spec convention.
 */
public class ByteLabeller implements Decoder {

    /**
     * Label set bits (those = 1) in hex.
     */
    public static String labelFor(String hex) {
        StringBuilder label = new StringBuilder();
        for (EmvBit bit : EmvBit.fromHex(hex)) {
            if (bit.isSet()) {
                if (label.length() > 0) {
                    label.append(",");
                }
                label.append("Byte ").append(bit.getByteNumber()).append(" Bit ").append(bit.getBitNumber());
            }
        }
        return label.toString();
    }

    @Override
    public List<DecodedData> decode(String input, int startIndexInBytes, DecodeSession decodeSession) {
        List<DecodedData> decoded = new ArrayList<>();
        for (EmvBit bit : EmvBit.fromHex(input)) {
            int byteIndex = startIndexInBytes + bit.getByteNumber() - 1;
            decoded.add(new DecodedData(bit.toString(), "", byteIndex, byteIndex + 1));
        }
        return decoded;
    }

    @Override
    public String validate(String input) {
        if ((input.length() % 2) != 0) {
            return "Must be an even number of characters";
        }
        return null;
    }

    @Override
    public int getMaxLength() {
        return Integer.MAX_VALUE;
    }
}
