package io.github.binaryfoo.decoders;

import io.github.binaryfoo.tlv.ISOUtil;

public class SometimesAsciiPrimitiveDecoder implements PrimitiveDecoder {
    @Override
    public String decode(String hexString) {
        StringBuilder builder = new StringBuilder();
        for (byte b : ISOUtil.hex2byte(hexString)) {
            if (Character.isISOControl(b)) {
                return hexString;
            }
            builder.append((char) b);
        }
        return builder.toString();
    }
}
