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
            decoded.findAllForTag(EmvTags.ICC_PUBLIC_KEY_CERTIFICATE).forEach { it.notes = result.text }
        }
    }

}

fun decodeICCPublicKeyCertificate(recovered: ByteArray, byteLengthOfIssuerModulus: Int): RecoveredPublicKeyCertificate {
    val b = StringBuilder()
    b.append("Header: ").append(ISOUtil.hexString(recovered, 0, 1)).append('\n')
    b.append("Format: ").append(ISOUtil.hexString(recovered, 1, 1)).append('\n')
    b.append("PAN: ").append(ISOUtil.hexString(recovered, 2, 10)).append('\n')
    b.append("Expiry Date (MMYY): ").append(ISOUtil.hexString(recovered, 12, 2)).append('\n')
    b.append("Serial number: ").append(ISOUtil.hexString(recovered, 14, 3)).append('\n')
    b.append("Hash algorithm: ").append(ISOUtil.hexString(recovered, 17, 1)).append('\n')
    b.append("Public key algorithm: ").append(ISOUtil.hexString(recovered, 18, 1)).append('\n')
    val publicKeyLength = Integer.parseInt(ISOUtil.hexString(recovered, 19, 1), 16)
    b.append("Public key length: ").append(publicKeyLength).append('\n')
    val exponentLength = ISOUtil.hexString(recovered, 20, 1)
    b.append("Public key exponent length: ").append(exponentLength).append('\n')
    var lengthOfLeftKeyPart = if (publicKeyLength > byteLengthOfIssuerModulus - 42) byteLengthOfIssuerModulus - 42 else publicKeyLength
    val leftKeyPart = ISOUtil.hexString(recovered, 21, lengthOfLeftKeyPart)
    b.append("Public key: ").append(leftKeyPart).append('\n')
    b.append("Hash: ").append(ISOUtil.hexString(recovered, 21 + byteLengthOfIssuerModulus - 42, 20)).append('\n')
    b.append("Trailer: ").append(ISOUtil.hexString(recovered, 21 + byteLengthOfIssuerModulus - 42 + 20, 1)).append('\n')
    return RecoveredPublicKeyCertificate("ICC", b.toString(), exponentLength, leftKeyPart)
}
