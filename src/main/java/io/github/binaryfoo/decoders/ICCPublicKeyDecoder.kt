package io.github.binaryfoo.decoders

import io.github.binaryfoo.tlv.ISOUtil
import io.github.binaryfoo.decoders.annotator.Annotater
import io.github.binaryfoo.EmvTags
import io.github.binaryfoo.crypto.RecoveredPublicKeyCertificate
import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.findForTag
import io.github.binaryfoo.findAllForTag
import io.github.binaryfoo.HexDumpFactory

public class ICCPublicKeyDecoder : Annotater {

    override fun createNotes(session: DecodeSession, decoded: List<DecodedData>) {
        val recoveredIssuerPublicKeyCertificate = session.issuerPublicKeyCertificate
        val iccCertificate = session.findTlv(EmvTags.ICC_PUBLIC_KEY_CERTIFICATE)

        if (iccCertificate != null && recoveredIssuerPublicKeyCertificate != null && recoveredIssuerPublicKeyCertificate.exponent != null) {
            for (decodedCertificate in decoded.findAllForTag(EmvTags.ICC_PUBLIC_KEY_CERTIFICATE)) {
                val startIndex = decodedCertificate.startIndex + iccCertificate.startIndexOfValue
                val result = recoverCertificate(iccCertificate.valueAsHexString, recoveredIssuerPublicKeyCertificate, startIndex, ::decodeICCPublicKeyCertificate)
                val certificate = result.certificate
                if (certificate != null) {
                    certificate.rightKeyPart = session.findTag(EmvTags.ICC_PUBLIC_KEY_REMAINDER)
                    certificate.exponent = session.findTag(EmvTags.ICC_PUBLIC_KEY_EXPONENT)
                    session.iccPublicKeyCertificate = certificate
                }
                decodedCertificate.addChildren(result.decoded)
                if (result.recoveredHex != null) {
                    decodedCertificate.hexDump = HexDumpFactory().splitIntoByteLengthStrings(result.recoveredHex, startIndex)
                }
            }
        }
    }

}

fun decodeICCPublicKeyCertificate(recovered: ByteArray, byteLengthOfIssuerModulus: Int, startIndexInBytes: Int): RecoveredPublicKeyCertificate {
    val publicKeyLength = Integer.parseInt(ISOUtil.hexString(recovered, 19, 1), 16)
    val exponentLength = ISOUtil.hexString(recovered, 20, 1)
    var lengthOfLeftKeyPart = if (publicKeyLength > byteLengthOfIssuerModulus - 42) byteLengthOfIssuerModulus - 42 else publicKeyLength
    val leftKeyPart = ISOUtil.hexString(recovered, 21, lengthOfLeftKeyPart)
    val children = listOf(
        DecodedData.byteRange("Header", recovered, 0, 1, startIndexInBytes),
        DecodedData.byteRange("Format", recovered, 1, 1, startIndexInBytes),
        DecodedData.byteRange("PAN", recovered, 2, 10, startIndexInBytes),
        DecodedData.byteRange("Expiry Date (MMYY)", recovered, 12, 2, startIndexInBytes),
        DecodedData.byteRange("Serial number", recovered, 14, 3, startIndexInBytes),
        DecodedData.byteRange("Hash algorithm", recovered, 17, 1, startIndexInBytes),
        DecodedData.byteRange("Public key algorithm", recovered, 18, 1, startIndexInBytes),
        DecodedData.byteRange("Public key length", publicKeyLength.toString(), 19, 1, startIndexInBytes),
        DecodedData.byteRange("Public key exponent length", exponentLength, 20, 1, startIndexInBytes),
        DecodedData.byteRange("Public key", leftKeyPart, 21, lengthOfLeftKeyPart, startIndexInBytes),
        DecodedData.byteRange("Hash", recovered, 21 + byteLengthOfIssuerModulus - 42, 20, startIndexInBytes),
        DecodedData.byteRange("Trailer", recovered, 21 + byteLengthOfIssuerModulus - 42 + 20, 1, startIndexInBytes)
    )
    return RecoveredPublicKeyCertificate("ICC", children, exponentLength, leftKeyPart)
}
