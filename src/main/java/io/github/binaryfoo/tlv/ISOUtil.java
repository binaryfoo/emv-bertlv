package io.github.binaryfoo.tlv;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class ISOUtil {
    private static final Pattern HEX_CHARACTERS = Pattern.compile("[0-9a-fA-F]+");

    /**
     * converts a byte array to hex string
     * (suitable for dumps and ASCII packaging of Binary fields
     *
     * @param b - byte array
     * @return String representation
     */
    public static @NotNull String hexString(byte[] b) {
        return hexString(b, 0, b.length);
    }

    public static @NotNull String hexString(byte[] b, int offset, int len) {
        StringBuilder d = new StringBuilder(b.length * 2);
        for (int i = 0; i < len; i++) {
            char hi = Character.forDigit((b[offset + i] >> 4) & 0x0F, 16);
            char lo = Character.forDigit(b[offset + i] & 0x0F, 16);
            d.append(Character.toUpperCase(hi));
            d.append(Character.toUpperCase(lo));
        }
        return d.toString();
    }

    /**
     * @param b      source byte array
     * @param offset starting offset
     * @param len    number of bytes in destination (processes len*2)
     * @return byte[len]
     */
    public static byte[] hex2byte(byte[] b, int offset, int len) {
        byte[] d = new byte[len];
        for (int i = 0; i < len * 2; i++) {
            int shift = i % 2 == 1 ? 0 : 4;
            d[i >> 1] |= Character.digit((char) b[offset + i], 16) << shift;
        }
        return d;
    }

    /**
     * @param s source string (with Hex representation)
     * @return byte array
     */
    public static @NotNull byte[] hex2byte(String s) {
        if (s.length() % 2 == 0) {
            return hex2byte(s.getBytes(), 0, s.length() >> 1);
        } else {
            throw new RuntimeException("Uneven number(" + s.length() + ") of hex digits passed to hex2byte.");
        }
    }

    /**
     * converts a byte array to printable characters
     *
     * @param b - byte array
     * @return String representation
     */
    public static String dumpString(byte[] b) {
        StringBuilder d = new StringBuilder(b.length * 2);
        for (byte aB : b) {
            char c = (char) aB;
            if (Character.isISOControl(c)) {
                switch (c) {
                    case '\r':
                        d.append("{CR}");
                        break;
                    case '\n':
                        d.append("{LF}");
                        break;
                    case '\000':
                        d.append("{NULL}");
                        break;
                    default:
                        char hi = Character.forDigit((aB >> 4) & 0x0F, 16);
                        char lo = Character.forDigit(aB & 0x0F, 16);
                        d.append('[').append(Character.toUpperCase(hi)).append(Character.toUpperCase(lo)).append(']');
                        break;
                }
            } else
                d.append(c);

        }
        return d.toString();
    }

    public static boolean isValidHexString(String s) {
        return HEX_CHARACTERS.matcher(s).matches();
    }
}
