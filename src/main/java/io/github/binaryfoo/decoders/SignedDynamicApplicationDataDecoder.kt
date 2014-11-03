package io.github.binaryfoo.decoders

import io.github.binaryfoo.tlv.ISOUtil
import io.github.binaryfoo.decoders.annotator.Annotater
import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.EmvTags
import io.github.binaryfoo.findForValue
import io.github.binaryfoo.findAllForValue
import io.github.binaryfoo.findForTag
import io.github.binaryfoo.crypto.RecoveredPublicKeyCertificate

/**
 * Dynamic data auth means CA (scheme) -> Issuer -> ICC -> data
 *
 * Covers both DDA (the first step up from SDA) and CDA (combined with Generate AC)
 */
public class SignedDynamicApplicationDataDecoder : Annotater {
    override fun createNotes(session: DecodeSession, decoded: List<DecodedData>) {
        val iccPublicKeyCertificate = session.iccPublicKeyCertificate
        val signedData = session.signedDynamicAppData ?: decoded.findForTag(EmvTags.SIGNED_DYNAMIC_APPLICATION_DATA)?.fullDecodedData
        if (signedData != null && iccPublicKeyCertificate != null) {
            val notes = recoverText(signedData, iccPublicKeyCertificate, ::decodeSignedDynamicData)
            decoded.findAllForValue(signedData).forEach { it.notes = notes }
        }
    }

}

fun decodeSignedDynamicData(recovered: ByteArray, byteLengthOfICCModulus: Int): String {
    val b = StringBuilder()
    b.append("Header: ").append(ISOUtil.hexString(recovered, 0, 1)).append('\n')
    b.append("Format: ").append(ISOUtil.hexString(recovered, 1, 1)).append('\n')
    b.append("Hash algorithm: ").append(ISOUtil.hexString(recovered, 2, 1)).append('\n')
    val dynamicDataLength = Integer.parseInt(ISOUtil.hexString(recovered, 3, 1), 16)
    b.append("Dynamic data length: ").append(dynamicDataLength).append('\n')
    val iccDynamicNumberLength = Integer.parseInt(ISOUtil.hexString(recovered, 4, 1), 16)
    b.append("ICC dynamic number length: ").append(iccDynamicNumberLength).append('\n')
    b.append("ICC dynamic number: ").append(ISOUtil.hexString(recovered, 5, iccDynamicNumberLength)).append('\n')
    val cryptogramInformationData = ISOUtil.hexString(recovered, 5 + iccDynamicNumberLength, 1)
    if (cryptogramInformationData != "BB") {
        b.append("Cryptogram information data: ").append(cryptogramInformationData).append('\n')
        b.append("Cryptogram: ").append(ISOUtil.hexString(recovered, 5 + iccDynamicNumberLength + 1, 8)).append('\n')
        b.append("Transaction data hash code: ").append(ISOUtil.hexString(recovered, 5 + iccDynamicNumberLength + 1 + 8, 20)).append('\n')
    }
    b.append("Hash: ").append(ISOUtil.hexString(recovered, recovered.size - 21, 20)).append('\n')
    b.append("Trailer: ").append(ISOUtil.hexString(recovered, recovered.size - 1, 1)).append('\n')
    return b.toString()
}
