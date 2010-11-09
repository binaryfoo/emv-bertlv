package com.willcurrie.tlv;

import java.util.regex.Pattern;

public class ISOUtil {
	private static final Pattern HEX_CHARACTERS = Pattern.compile("[0-9a-fA-F]+");
	
	/**
     * converts a byte array to hex string 
     * (suitable for dumps and ASCII packaging of Binary fields
     * @param b - byte array
     * @return String representation
     */
    public static String hexString(byte[] b) {
        StringBuffer d = new StringBuffer(b.length * 2);
        for (int i=0; i<b.length; i++) {
            char hi = Character.forDigit ((b[i] >> 4) & 0x0F, 16);
            char lo = Character.forDigit (b[i] & 0x0F, 16);
            d.append(Character.toUpperCase(hi));
            d.append(Character.toUpperCase(lo));
        }
        return d.toString();
    }
    /**
     * @param   b       source byte array
     * @param   offset  starting offset
     * @param   len     number of bytes in destination (processes len*2)
     * @return  byte[len]
     */
    public static byte[] hex2byte (byte[] b, int offset, int len) {
        byte[] d = new byte[len];
        for (int i=0; i<len*2; i++) {
            int shift = i%2 == 1 ? 0 : 4;
            d[i>>1] |= Character.digit((char) b[offset+i], 16) << shift;
        }
        return d;
    }
    /**
     * @param s source string (with Hex representation)
     * @return byte array
     */
    public static byte[] hex2byte (String s) {
        if (s.length() % 2 == 0) {
            return hex2byte (s.getBytes(), 0, s.length() >> 1);
        } else {
            throw new RuntimeException("Uneven number("+s.length()+") of hex digits passed to hex2byte.");
        }
    }
    
    /**
     * converts a byte array to printable characters
     * @param b - byte array
     * @return String representation
     */
    public static String dumpString(byte[] b) {
        StringBuffer d = new StringBuffer(b.length * 2);
        for (int i=0; i<b.length; i++) {
            char c = (char) b[i];
            if (Character.isISOControl (c)) {
                // TODO: complete list of control characters,
                // use a String[] instead of this weird switch
                switch (c) {
                    case '\r'  : d.append ("{CR}");   break;
                    case '\n'  : d.append ("{LF}");   break;
                    case '\000': d.append ("{NULL}"); break;
                    case '\001': d.append ("{SOH}");  break;
                    case '\002': d.append ("{STX}");  break;
                    case '\003': d.append ("{ETX}");  break;
                    case '\004': d.append ("{EOT}");  break;
                    case '\005': d.append ("{ENQ}");  break;
                    case '\006': d.append ("{ACK}");  break;
                    case '\007': d.append ("{BEL}");  break;
                    case '\020': d.append ("{DLE}");  break;
                    case '\025': d.append ("{NAK}");  break;
                    case '\026': d.append ("{SYN}");  break;
                    case '\034': d.append ("{FS}");  break;
                    case '\036': d.append ("{RS}");  break;
                    default:
                        char hi = Character.forDigit ((b[i] >> 4) & 0x0F, 16);
                        char lo = Character.forDigit (b[i] & 0x0F, 16);
                        d.append('[');
                        d.append(Character.toUpperCase(hi));
                        d.append(Character.toUpperCase(lo));
                        d.append(']');
                        break;
                }
            }
            else
                d.append (c);

        }
        return d.toString();
    }
    
    public static boolean isValidHexString(String s) {
    	return HEX_CHARACTERS.matcher(s).matches();
    }
}
