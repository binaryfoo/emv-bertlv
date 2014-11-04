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
import io.github.binaryfoo.HexDumpFactory

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
                for (decodedCertificate in decoded.findAllForTag(EmvTags.ISSUER_PUBLIC_KEY_CERTIFICATE)) {
                    val result = recoverCertificate(encryptedCertificate, caPublicKey, decodedCertificate.startIndex, ::decodeIssuerPublicKey)
                    val certificate = result.certificate
                    if (certificate != null) {
                        certificate.rightKeyPart = session.findTag(EmvTags.ISSUER_PUBLIC_KEY_REMAINDER)
                        certificate.exponent = session.findTag(EmvTags.ISSUER_PUBLIC_KEY_EXPONENT)
                        session.issuerPublicKeyCertificate = certificate
                    }
                    decodedCertificate.addChildren(result.decoded)
                    if (result.recoveredHex != null) {
                        decodedCertificate.hexDump = HexDumpFactory().splitIntoByteLengthStrings(result.recoveredHex, decodedCertificate.startIndex)
                    }
                }
            }
        }
    }

    fun extractRid(fileName: String?) = fileName?.substring(0, 10)

}

fun decodeIssuerPublicKey(recovered: ByteArray, byteLengthOfCAModulus: Int, startIndexInBytes: Int): RecoveredPublicKeyCertificate {
    val publicKeyLength = Integer.parseInt(ISOUtil.hexString(recovered, 13, 1), 16)
    val exponentLength = ISOUtil.hexString(recovered, 14, 1)
    var lengthOfLeftKeyPart = if (publicKeyLength > byteLengthOfCAModulus - 36) byteLengthOfCAModulus - 36 else publicKeyLength
    val leftKeyPart = ISOUtil.hexString(recovered, 15, lengthOfLeftKeyPart)
    val b = listOf(
        DecodedData.byteRange("Header", recovered, 0, 1, startIndexInBytes),
        DecodedData.byteRange("Format", recovered, 1, 1, startIndexInBytes),
        DecodedData.byteRange("Identifier (PAN prefix)", recovered, 2, 4, startIndexInBytes),
        DecodedData.byteRange("Expiry Date (MMYY)", recovered, 6, 2, startIndexInBytes),
        DecodedData.byteRange("Serial number", recovered, 8, 3, startIndexInBytes),
        DecodedData.byteRange("Hash algorithm", recovered, 11, 1, startIndexInBytes),
        DecodedData.byteRange("Public key algorithm", recovered, 12, 1, startIndexInBytes),
        DecodedData.byteRange("Public key length", publicKeyLength.toString(), 13, 1, startIndexInBytes),
        DecodedData.byteRange("Public key exponent length", exponentLength, 14, 1, startIndexInBytes),
        DecodedData.byteRange("Public key", leftKeyPart, 15, lengthOfLeftKeyPart, startIndexInBytes),
        DecodedData.byteRange("Hash", recovered, 15 + byteLengthOfCAModulus - 36, 20, startIndexInBytes),
        DecodedData.byteRange("Trailer", recovered, 15 + byteLengthOfCAModulus - 36 + 20, 1, startIndexInBytes)
    )
    return RecoveredPublicKeyCertificate("Issuer", b, exponentLength, leftKeyPart)
}