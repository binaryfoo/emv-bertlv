package io.github.binaryfoo.decoders

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.Decoder
import io.github.binaryfoo.tlv.ISOUtil
import io.github.binaryfoo.decoders.annotator.Annotater
import io.github.binaryfoo.tlv.BerTlv
import io.github.binaryfoo.EmvTags
import io.github.binaryfoo.crypto

public class IssuerPublicKeyDecoder : Annotater {

    override fun createNotes(decodeSession: DecodeSession): String? {
        val keyIndex = decodeSession.findTag(EmvTags.CA_PUBLIC_KEY_INDEX)
        val certificate = decodeSession.findTag(EmvTags.ISSUER_PUBLIC_KEY_CERTIFICATE)
        val aid = extractAid(decodeSession.findTag(EmvTags.DEDICATED_FILE_NAME))
        if (keyIndex != null && certificate != null && aid != null) {
            val caPublicKey = crypto.CaPublicKeyTable.getEntry(aid, keyIndex)
            if (caPublicKey != null) {
                val recovered = SignedDataRecoverer().recover(certificate, caPublicKey)
                val certificateRemainder = decodeSession.findTag(EmvTags.ISSUER_PUBLIC_KEY_REMAINDER)
                // TODO store public key for further decryption
                return decode(recovered, caPublicKey.modulus.size)
            }
        }
        return null
    }

    public fun decode(recovered: ByteArray, byteLengthOfCAModulus: Int): String {
        val b = StringBuilder()
        b.append("Header: ").append(ISOUtil.hexString(recovered, 0, 1)).append('\n')
        b.append("Format: ").append(ISOUtil.hexString(recovered, 1, 1)).append('\n')
        b.append("Identifier (PAN prefix): ").append(ISOUtil.hexString(recovered, 2, 4)).append('\n')
        b.append("Expiry Date (MMYY): ").append(ISOUtil.hexString(recovered, 6, 2)).append('\n')
        b.append("Serial number: ").append(ISOUtil.hexString(recovered, 8, 3)).append('\n')
        b.append("Hash algorithm: ").append(ISOUtil.hexString(recovered, 11, 1)).append('\n')
        b.append("Public key algorithm: ").append(ISOUtil.hexString(recovered, 12, 1)).append('\n')
        val publicKeyLength = ISOUtil.hexString(recovered, 13, 1)
        val i = Integer.parseInt(publicKeyLength, 16)
        b.append("Public key length: ").append(publicKeyLength).append(" (").append(i).append(")").append('\n')
        b.append("Public key exponent length: ").append(ISOUtil.hexString(recovered, 14, 1)).append('\n')
        b.append("Public key: ").append(ISOUtil.hexString(recovered, 15, byteLengthOfCAModulus - 36)).append('\n')
        b.append("          : ").append(ISOUtil.hexString(recovered, 15, i)).append('\n')
        b.append("Hash: ").append(ISOUtil.hexString(recovered, 15 + byteLengthOfCAModulus - 36, 20)).append('\n')
        b.append("Trailer: ").append(ISOUtil.hexString(recovered, 15 + byteLengthOfCAModulus - 36 + 20, 1)).append('\n')
        return b.toString()
    }

    fun extractAid(fileName: String?) = fileName?.substring(0, 10)
}
