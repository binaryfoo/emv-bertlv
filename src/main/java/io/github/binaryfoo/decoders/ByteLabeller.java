package io.github.binaryfoo.decoders;

public class ByteLabeller {
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
}
