package com.willcurrie.decoders;

import com.willcurrie.tlv.ISOUtil;

public class AsciiPrimitiveDecoder implements PrimitiveDecoder {
    @Override
    public String decode(String hexString) {
        return ISOUtil.dumpString(ISOUtil.hex2byte(hexString));
    }
}
