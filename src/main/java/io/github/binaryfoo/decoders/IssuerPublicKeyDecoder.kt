package io.github.binaryfoo.decoders

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.Decoder
import io.github.binaryfoo.tlv.ISOUtil
import io.github.binaryfoo.decoders.annotator.Annotater
import io.github.binaryfoo.tlv.BerTlv
import io.github.binaryfoo.EmvTags
import io.github.binaryfoo.crypto
import io.github.binaryfoo.crypto.IssuerPublicKeyCertificate

/**
 * EMV 4.3 Book2, Table 6: Format of Data Recovered from Issuer Public Key Certificate
 */
public class IssuerPublicKeyDecoder : Annotater {

    override fun createNotes(session: DecodeSession): String? {
        val keyIndex = session.findTag(EmvTags.CA_PUBLIC_KEY_INDEX)
        val certificate = session.findTag(EmvTags.ISSUER_PUBLIC_KEY_CERTIFICATE)
        val rid = extractRid(session.findTag(EmvTags.DEDICATED_FILE_NAME))
        if (keyIndex != null && certificate != null && rid != null) {
            val caPublicKey = crypto.CaPublicKeyTable.getEntry(rid, keyIndex)
            if (caPublicKey != null) {
                val recovered = SignedDataRecoverer().recover(certificate, caPublicKey)
                val issuerPublicKeyCertificate = decode(recovered, caPublicKey.modulus.size)

                issuerPublicKeyCertificate.rightKeyPart = session.findTag(EmvTags.ISSUER_PUBLIC_KEY_REMAINDER)
                session.issuerPublicKeyCertificate = issuerPublicKeyCertificate
                return issuerPublicKeyCertificate.textDump
            }
        }
        return null
    }

    public fun decode(recovered: ByteArray, byteLengthOfCAModulus: Int): IssuerPublicKeyCertificate {
        val b = StringBuilder()
        b.append("Header: ").append(ISOUtil.hexString(recovered, 0, 1)).append('\n')
        b.append("Format: ").append(ISOUtil.hexString(recovered, 1, 1)).append('\n')
        b.append("Identifier (PAN prefix): ").append(ISOUtil.hexString(recovered, 2, 4)).append('\n')
        b.append("Expiry Date (MMYY): ").append(ISOUtil.hexString(recovered, 6, 2)).append('\n')
        b.append("Serial number: ").append(ISOUtil.hexString(recovered, 8, 3)).append('\n')
        b.append("Hash algorithm: ").append(ISOUtil.hexString(recovered, 11, 1)).append('\n')
        b.append("Public key algorithm: ").append(ISOUtil.hexString(recovered, 12, 1)).append('\n')
        val publicKeyLength = Integer.parseInt(ISOUtil.hexString(recovered, 13, 1), 16)
        b.append("Public key length: ").append(publicKeyLength).append('\n')
        val exponentLength = ISOUtil.hexString(recovered, 14, 1)
        b.append("Public key exponent length: ").append(exponentLength).append('\n')
        var lengthOfLeftKeyPart = if (publicKeyLength > byteLengthOfCAModulus - 36) byteLengthOfCAModulus - 36 else publicKeyLength
        val leftKeyPart = ISOUtil.hexString(recovered, 15, lengthOfLeftKeyPart)
        b.append("Public key: ").append(leftKeyPart).append('\n')
        b.append("Hash: ").append(ISOUtil.hexString(recovered, 15 + byteLengthOfCAModulus - 36, 20)).append('\n')
        b.append("Trailer: ").append(ISOUtil.hexString(recovered, 15 + byteLengthOfCAModulus - 36 + 20, 1)).append('\n')
        return IssuerPublicKeyCertificate(b.toString(), exponentLength, leftKeyPart)
    }

    fun extractRid(fileName: String?) = fileName?.substring(0, 10)
}
