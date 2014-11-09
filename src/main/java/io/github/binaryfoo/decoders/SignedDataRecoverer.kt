package io.github.binaryfoo.decoders

import io.github.binaryfoo.crypto.CaPublicKey
import io.github.binaryfoo.tlv.ISOUtil

import java.math.BigInteger

/**
 * RSA public key decryption
 */
public class SignedDataRecoverer {

    public fun recover(signed: String, exponent: String, modulus: String): ByteArray {
        return recover(ISOUtil.hex2byte(signed), ISOUtil.hex2byte(exponent), ISOUtil.hex2byte(modulus))
    }

    public fun recover(signed: ByteArray, exponent: ByteArray, modulus: ByteArray): ByteArray {
        val biSigned = BigInteger(1, signed)
        val biExponent = BigInteger(exponent)
        val bigModulus = BigInteger(1, modulus)
        val recovered = biSigned.modPow(biExponent, bigModulus).toByteArray()
        verify(recovered)
        return recovered
    }

    private fun verify(recovered: ByteArray) {
        val header = recovered[0].toInt()
        if (header != 0x6A) {
            throw IllegalStateException("Recover failed: bad header byte ${Integer.toHexString(header)}")
        }
        val footer = recovered[recovered.lastIndex].toInt() and 0xFF
        if (footer != 0xBC) {
            throw IllegalStateException("Recover failed: bad footer byte ${Integer.toHexString(footer)}")
        }
    }

}
