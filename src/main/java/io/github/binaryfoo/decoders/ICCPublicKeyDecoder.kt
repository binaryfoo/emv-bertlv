package io.github.binaryfoo.decoders

import io.github.binaryfoo.tlv.ISOUtil
import io.github.binaryfoo.decoders.annotator.Annotater
import io.github.binaryfoo.EmvTags
import io.github.binaryfoo.crypto.RecoveredPublicKeyCertificate
import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.findForTag
import io.github.binaryfoo.findAllForTag

public class ICCPublicKeyDecoder : Annotater {

    override fun createNotes(session: DecodeSession, decoded: List<DecodedData>) {
        val recoveredIssuerPublicKeyCertificate = session.issuerPublicKeyCertificate
        val iccCertificate = session.findTag(EmvTags.ICC_PUBLIC_KEY_CERTIFICATE)

        if (iccCertificate != null && recoveredIssuerPublicKeyCertificate != null && recoveredIssuerPublicKeyCertificate.exponent != null) {
            val result = recoverCertificate(iccCertificate, recoveredIssuerPublicKeyCertificate, ::decodeICCPublicKeyCertificate)
            val certificate = result.certificate
            if (certificate != null) {
                certificate.rightKeyPart = session.findTag(EmvTags.ICC_PUBLIC_KEY_REMAINDER)
                certificate.exponent = session.findTag(EmvTags.ICC_PUBLIC_KEY_EXPONENT)
                session.iccPublicKeyCertificate = certificate
            }
            decoded.findAllForTag(EmvTags.ICC_PUBLIC_KEY_CERTIFICATE).forEach { it.addChildren(result.decoded) }
        }
    }

}

fun decodeICCPublicKeyCertificate(recovered: ByteArray, byteLengthOfIssuerModulus: Int): RecoveredPublicKeyCertificate {
    val publicKeyLength = Integer.parseInt(ISOUtil.hexString(recovered, 19, 1), 16)
    val exponentLength = ISOUtil.hexString(recovered, 20, 1)
    var lengthOfLeftKeyPart = if (publicKeyLength > byteLengthOfIssuerModulus - 42) byteLengthOfIssuerModulus - 42 else publicKeyLength
    val leftKeyPart = ISOUtil.hexString(recovered, 21, lengthOfLeftKeyPart)
    val children = listOf(
        DecodedData.primitive("Header", ISOUtil.hexString(recovered, 0, 1)),
        DecodedData.primitive("Format", ISOUtil.hexString(recovered, 1, 1)),
        DecodedData.primitive("PAN", ISOUtil.hexString(recovered, 2, 10)),
        DecodedData.primitive("Expiry Date (MMYY)", ISOUtil.hexString(recovered, 12, 2)),
        DecodedData.primitive("Serial number", ISOUtil.hexString(recovered, 14, 3)),
        DecodedData.primitive("Hash algorithm", ISOUtil.hexString(recovered, 17, 1)),
        DecodedData.primitive("Public key algorithm", ISOUtil.hexString(recovered, 18, 1)),
        DecodedData.primitive("Public key length", publicKeyLength.toString()),
        DecodedData.primitive("Public key exponent length", exponentLength),
        DecodedData.primitive("Public key", leftKeyPart),
        DecodedData.primitive("Hash", ISOUtil.hexString(recovered, 21 + byteLengthOfIssuerModulus - 42, 20)),
        DecodedData.primitive("Trailer", ISOUtil.hexString(recovered, 21 + byteLengthOfIssuerModulus - 42 + 20, 1))
    )
    return RecoveredPublicKeyCertificate("ICC", children, exponentLength, leftKeyPart)
}
