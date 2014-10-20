package io.github.binaryfoo.decoders

import io.github.binaryfoo.tlv.ISOUtil

public class SignedDynamicApplicationDataDecoder {

    public fun decode(recovered: ByteArray, byteLengthOfICCModulus: Int): String {
        val b = StringBuilder()
        b.append("Header: ").append(ISOUtil.hexString(recovered, 0, 1)).append('\n')
        b.append("Format: ").append(ISOUtil.hexString(recovered, 1, 1)).append('\n')
        b.append("Hash algorithm: ").append(ISOUtil.hexString(recovered, 2, 1)).append('\n')
        val dynamicDataLength = ISOUtil.hexString(recovered, 3, 1)
        val i = Integer.parseInt(dynamicDataLength, 16)
        b.append("Dynamic data length: ").append(dynamicDataLength).append(" (").append(i).append(")").append('\n')
        b.append("Dynamic data: ").append(ISOUtil.hexString(recovered, 4, i)).append('\n')
        b.append("Hash: ").append(ISOUtil.hexString(recovered, 4 + (byteLengthOfICCModulus - i - 25), 20)).append('\n')
        b.append("Trailer: ").append(ISOUtil.hexString(recovered, 4 + (byteLengthOfICCModulus - i - 25) + 20, 1)).append('\n')
        return b.toString()
    }
}
