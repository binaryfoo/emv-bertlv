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
            val result = recoverText(signedData, iccPublicKeyCertificate, ::decodeSignedDynamicData)
            decoded.findAllForValue(signedData).forEach { it.addChildren(result.decoded) }
        }
    }

}

fun decodeSignedDynamicData(recovered: ByteArray, byteLengthOfICCModulus: Int): List<DecodedData> {
    val dynamicDataLength = Integer.parseInt(ISOUtil.hexString(recovered, 3, 1), 16)
    val iccDynamicNumberLength = Integer.parseInt(ISOUtil.hexString(recovered, 4, 1), 16)
    val cryptogramInformationData = ISOUtil.hexString(recovered, 5 + iccDynamicNumberLength, 1)
    return listOf(
        DecodedData.primitive("Header", ISOUtil.hexString(recovered, 0, 1)),
        DecodedData.primitive("Format", ISOUtil.hexString(recovered, 1, 1)),
        DecodedData.primitive("Hash algorithm", ISOUtil.hexString(recovered, 2, 1)),
        DecodedData.primitive("Dynamic data length", dynamicDataLength.toString()),
        DecodedData.primitive("ICC dynamic number length", iccDynamicNumberLength.toString()),
        DecodedData.primitive("ICC dynamic number", ISOUtil.hexString(recovered, 5, iccDynamicNumberLength)),
        *if (cryptogramInformationData != "BB") {
            array(
                DecodedData.primitive("Cryptogram information data", cryptogramInformationData),
                DecodedData.primitive("Cryptogram", ISOUtil.hexString(recovered, 5 + iccDynamicNumberLength + 1, 8)),
                DecodedData.primitive("Transaction data hash code", ISOUtil.hexString(recovered, 5 + iccDynamicNumberLength + 1 + 8, 20))
            )
        } else {
            array()
        },
        DecodedData.primitive("Hash", ISOUtil.hexString(recovered, recovered.size - 21, 20)),
        DecodedData.primitive("Trailer", ISOUtil.hexString(recovered, recovered.size - 1, 1))
    )
}
