package io.github.binaryfoo.decoders

import io.github.binaryfoo.tlv.ISOUtil
import io.github.binaryfoo.decoders.annotator.SignedDataDecoder
import io.github.binaryfoo.EmvTags
import io.github.binaryfoo.crypto.RecoveredPublicKeyCertificate
import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.findAllForTag
import io.github.binaryfoo.findTlvForTag
import io.github.binaryfoo.findValueForTag

/**
 * EMV 4.3 Book2, Table 14: Format of Data Recovered from ICC Public Key Certificate
 */
public class ICCPublicKeyDecoder : SignedDataDecoder {

    override fun decodeSignedData(session: DecodeSession, decoded: List<DecodedData>) {
        val recoveredIssuerPublicKeyCertificate = session.issuerPublicKeyCertificate
        val iccCertificate = decoded.findTlvForTag(EmvTags.ICC_PUBLIC_KEY_CERTIFICATE)

        if (iccCertificate != null && recoveredIssuerPublicKeyCertificate != null && recoveredIssuerPublicKeyCertificate.exponent != null) {
            for (decodedCertificate in decoded.findAllForTag(EmvTags.ICC_PUBLIC_KEY_CERTIFICATE)) {
                val result = recoverCertificate(iccCertificate, decodedCertificate, recoveredIssuerPublicKeyCertificate, ::decodeICCPublicKeyCertificate)
                if (result.certificate != null) {
                    result.certificate.rightKeyPart = decoded.findValueForTag(EmvTags.ICC_PUBLIC_KEY_REMAINDER)
                    result.certificate.exponent = decoded.findValueForTag(EmvTags.ICC_PUBLIC_KEY_EXPONENT)
                    session.iccPublicKeyCertificate = result.certificate
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
