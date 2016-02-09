package io.github.binaryfoo.decoders

import io.github.binaryfoo.tlv.ISOUtil

class SometimesAsciiPrimitiveDecoder : PrimitiveDecoder {
    override fun decode(hexString: String): String {
        val builder = StringBuilder()
        for (b in ISOUtil.hex2byte(hexString)) {
            if (Character.isISOControl(b.toInt())) {
                return hexString
            }
            builder.append(b.toChar())
        }
        return builder.toString()
    }
}
