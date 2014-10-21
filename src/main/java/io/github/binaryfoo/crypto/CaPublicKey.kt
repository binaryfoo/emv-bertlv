package io.github.binaryfoo.crypto

import io.github.binaryfoo.tlv.ISOUtil

public data class CaPublicKey(val rid: String, val index: String, exponent: String, modulus: String) {
    public val exponent: ByteArray
    public val modulus: ByteArray

    {
        this.exponent = ISOUtil.hex2byte(exponent)
        this.modulus = ISOUtil.hex2byte(modulus)
    }
}
