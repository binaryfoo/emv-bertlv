package io.github.binaryfoo.decoders;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.Decoder;

import java.util.ArrayList;
import java.util.List;

/**
 * Label bytes left to right starting at 1.
 * Label bits within the byte right to left starting at 1.
 */
public class ByteLabeller implements Decoder {
    public static String labelFor(String hex) {
        StringBuilder label = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            int b = Integer.parseInt(hex.substring(i, i + 2), 16);
            for (int j = 0; j < 8; j++) {
                if ((b & 1 << j) != 0) {
                    if (label.length() > 0) {
                        label.append(",");
                    }
                    label.append("Byte ").append((i / 2) + 1).append(" Bit ").append(j + 1);
                }
            }
        }
        return label.toString();
    }

    @Override
    public List<DecodedData> decode(String input, int startIndexInBytes, DecodeSession decodeSession) {
        List<DecodedData> decoded = new ArrayList<>();
        for (int i = 0; i < input.length(); i += 2) {
            int b = Integer.parseInt(input.substring(i, i + 2), 16);
            int byteNumber = (i / 2) + 1;
            int byteIndex = startIndexInBytes + i / 2;
            for (int j = 7; j >= 0; j--) {
                String label = "Byte " + byteNumber + ", Bit " + (j + 1) + " = " + (b >> j & 0x1);
                decoded.add(new DecodedData(label, "", byteIndex, byteIndex + 1));
            }
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
