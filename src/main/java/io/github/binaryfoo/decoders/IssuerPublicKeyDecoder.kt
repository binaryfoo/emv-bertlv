package io.github.binaryfoo.decoders

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.Decoder
import io.github.binaryfoo.tlv.ISOUtil
import io.github.binaryfoo.decoders.annotator.Annotater
import io.github.binaryfoo.tlv.BerTlv
import io.github.binaryfoo.EmvTags
import io.github.binaryfoo.crypto
import io.github.binaryfoo.crypto.RecoveredPublicKeyCertificate
import io.github.binaryfoo.findForTag
import io.github.binaryfoo.findAllForTag

/**
 * EMV 4.3 Book2, Table 6: Format of Data Recovered from Issuer Public Key Certificate
 */
public class IssuerPublicKeyDecoder : Annotater {

    override fun createNotes(session: DecodeSession, decoded: List<DecodedData>) {
        val keyIndex = session.findTag(EmvTags.CA_PUBLIC_KEY_INDEX)
        val encryptedCertificate = session.findTag(EmvTags.ISSUER_PUBLIC_KEY_CERTIFICATE)
        val rid = extractRid(session.findTag(EmvTags.DEDICATED_FILE_NAME))
        if (keyIndex != null && encryptedCertificate != null && rid != null) {
            val caPublicKey = crypto.CaPublicKeyTable.getEntry(rid, keyIndex)
            if (caPublicKey != null) {
                val result = recoverCertificate(encryptedCertificate, caPublicKey, ::decodeIssuerPublicKey)
                val certificate = result.certificate
                if (certificate != null) {
                    certificate.rightKeyPart = session.findTag(EmvTags.ISSUER_PUBLIC_KEY_REMAINDER)
                    certificate.exponent = session.findTag(EmvTags.ISSUER_PUBLIC_KEY_EXPONENT)
                    session.issuerPublicKeyCertificate = certificate
                }
                decoded.findAllForTag(EmvTags.ISSUER_PUBLIC_KEY_CERTIFICATE).forEach { it.addChildren(result.decoded) }
            }
        }
    }

    fun extractRid(fileName: String?) = fileName?.substring(0, 10)

}
fun decodeIssuerPublicKey(recovered: ByteArray, byteLengthOfCAModulus: Int): RecoveredPublicKeyCertificate {
    val publicKeyLength = Integer.parseInt(ISOUtil.hexString(recovered, 13, 1), 16)
    val exponentLength = ISOUtil.hexString(recovered, 14, 1)
    var lengthOfLeftKeyPart = if (publicKeyLength > byteLengthOfCAModulus - 36) byteLengthOfCAModulus - 36 else publicKeyLength
    val leftKeyPart = ISOUtil.hexString(recovered, 15, lengthOfLeftKeyPart)
    val b = listOf(
        DecodedData.primitive("Header", ISOUtil.hexString(recovered, 0, 1)),
        DecodedData.primitive("Format", ISOUtil.hexString(recovered, 1, 1)),
        DecodedData.primitive("Identifier (PAN prefix)", ISOUtil.hexString(recovered, 2, 4)),
        DecodedData.primitive("Expiry Date (MMYY)", ISOUtil.hexString(recovered, 6, 2)),
        DecodedData.primitive("Serial number", ISOUtil.hexString(recovered, 8, 3)),
        DecodedData.primitive("Hash algorithm", ISOUtil.hexString(recovered, 11, 1)),
        DecodedData.primitive("Public key algorithm", ISOUtil.hexString(recovered, 12, 1)),
        DecodedData.primitive("Public key length", publicKeyLength.toString()),
        DecodedData.primitive("Public key exponent length", exponentLength),
        DecodedData.primitive("Public key", leftKeyPart),
        DecodedData.primitive("Hash", ISOUtil.hexString(recovered, 15 + byteLengthOfCAModulus - 36, 20)),
        DecodedData.primitive("Trailer", ISOUtil.hexString(recovered, 15 + byteLengthOfCAModulus - 36 + 20, 1))
    )
    return RecoveredPublicKeyCertificate("Issuer", b, exponentLength, leftKeyPart)
}
