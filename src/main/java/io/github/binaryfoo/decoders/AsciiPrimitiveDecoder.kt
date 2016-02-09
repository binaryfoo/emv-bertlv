package io.github.binaryfoo.decoders

import io.github.binaryfoo.tlv.ISOUtil

class AsciiPrimitiveDecoder : PrimitiveDecoder {
    override fun decode(hexString: String): String {
        return ISOUtil.dumpString(ISOUtil.hex2byte(hexString))
    }
}
