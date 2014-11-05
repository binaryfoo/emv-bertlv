package io.github.binaryfoo.decoders

import io.github.binaryfoo.tlv.ISOUtil
import io.github.binaryfoo.decoders.annotator.SignedDataDecoder
import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.EmvTags
import io.github.binaryfoo.findForValue
import io.github.binaryfoo.findAllForValue
import io.github.binaryfoo.findForTag
import io.github.binaryfoo.crypto.RecoveredPublicKeyCertificate
import io.github.binaryfoo.HexDumpFactory

/**
 * Dynamic data auth means CA (scheme) -> Issuer -> ICC -> data
 *
 * Covers both DDA (the first step up from SDA) and CDA (combined with Generate AC)
 *
 * EMV 4.3 Book2, Table 18: Dynamic Application Data to be Signed
 */
public class SignedDynamicApplicationDataDecoder : SignedDataDecoder {
    override fun createNotes(session: DecodeSession, decoded: List<DecodedData>) {
        val iccPublicKeyCertificate = session.iccPublicKeyCertificate
        val signedData = session.signedDynamicAppData ?: decoded.findForTag(EmvTags.SIGNED_DYNAMIC_APPLICATION_DATA)?.fullDecodedData
        if (signedData != null && iccPublicKeyCertificate != null) {
            for (decodedSignedData in decoded.findAllForValue(signedData)) {
                val offset = if (decodedSignedData.tag != null) session.findTlv(decodedSignedData.tag)?.startIndexOfValue ?: 0 else 0
                val startIndex = decodedSignedData.startIndex + offset
                val result = recoverText(signedData, iccPublicKeyCertificate, startIndex, ::decodeSignedDynamicData)
                decodedSignedData.addChildren(result.decoded)
                if (result.recoveredHex != null) {
                    decodedSignedData.hexDump = HexDumpFactory().splitIntoByteLengthStrings(result.recoveredHex, startIndex)
                }
            }
        }
    }

}

fun decodeSignedDynamicData(recovered: ByteArray, startIndexInBytes: Int): List<DecodedData> {
    val dynamicDataLength = Integer.parseInt(ISOUtil.hexString(recovered, 3, 1), 16)
    val iccDynamicNumberLength = Integer.parseInt(ISOUtil.hexString(recovered, 4, 1), 16)
    val cryptogramInformationData = ISOUtil.hexString(recovered, 5 + iccDynamicNumberLength, 1)
    return listOf(
        DecodedData.byteRange("Header", recovered, 0, 1, startIndexInBytes),
        DecodedData.byteRange("Format", recovered, 1, 1, startIndexInBytes),
        DecodedData.byteRange("Hash algorithm", recovered, 2, 1, startIndexInBytes),
        DecodedData.byteRange("Dynamic data length", dynamicDataLength.toString(), 3, 1, startIndexInBytes),
        DecodedData.byteRange("ICC dynamic number length", iccDynamicNumberLength.toString(), 4, 1, startIndexInBytes),
        DecodedData.byteRange("ICC dynamic number", recovered, 5, iccDynamicNumberLength, startIndexInBytes),
        *if (cryptogramInformationData != "BB") {
            array(
                DecodedData.byteRange("Cryptogram information data", cryptogramInformationData, 5 + iccDynamicNumberLength, 1, startIndexInBytes),
                DecodedData.byteRange("Cryptogram", recovered, 5 + iccDynamicNumberLength + 1, 8, startIndexInBytes),
                DecodedData.byteRange("Transaction data hash code", recovered, 5 + iccDynamicNumberLength + 1 + 8, 20, startIndexInBytes)
            )
        } else {
            array()
        },
        DecodedData.byteRange("Hash", recovered, recovered.size - 21, 20, startIndexInBytes),
        DecodedData.byteRange("Trailer", recovered, recovered.size - 1, 1, startIndexInBytes)
    )
}
