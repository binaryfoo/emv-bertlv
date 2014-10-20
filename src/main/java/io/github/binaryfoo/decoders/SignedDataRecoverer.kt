package io.github.binaryfoo.decoders

import io.github.binaryfoo.crypto.CaPublicKey
import io.github.binaryfoo.tlv.ISOUtil

import java.math.BigInteger

public class SignedDataRecoverer {

    public fun recover(signed: String, key: CaPublicKey): ByteArray {
        return recover(ISOUtil.hex2byte(signed), key.exponent, key.modulus)
    }

    public fun recover(signed: String, exponent: String, modulus: String): ByteArray {
        return recover(ISOUtil.hex2byte(signed), ISOUtil.hex2byte(exponent), ISOUtil.hex2byte(modulus))
    }

    public fun recover(signed: ByteArray, exponent: ByteArray, modulus: ByteArray): ByteArray {
        val biSigned = BigInteger(1, signed)
        val biExponent = BigInteger(exponent)
        val bigModulus = BigInteger(1, modulus)
        return biSigned.modPow(biExponent, bigModulus).toByteArray()
    }
}
