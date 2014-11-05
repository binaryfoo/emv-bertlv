package io.github.binaryfoo.decoders

import io.github.binaryfoo.decoders.annotator.Annotater
import io.github.binaryfoo.EmvTags
import io.github.binaryfoo.tlv.ISOUtil
import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.findForValue
import io.github.binaryfoo.findForTag
import io.github.binaryfoo.findAllForTag
import io.github.binaryfoo.HexDumpFactory

/**
 * EMV 4.3 Book 2, Table 7: Format of Data Recovered from Signed Static Application Data
 *
 * Static data auth means CA (scheme) -> Issuer -> data. Chip has no RSA hardware. No replay attack prevention.
 */
public class SignedStaticApplicationDataDecoder : Annotater {

    override fun createNotes(session: DecodeSession, decoded: List<DecodedData>) {
        val issuerPublicKeyCertificate = session.issuerPublicKeyCertificate
        val signedStaticData = session.findTlv(EmvTags.SIGNED_STATIC_APPLICATION_DATA)
        if (signedStaticData != null && issuerPublicKeyCertificate != null) {
            for (decodedSSD in decoded.findAllForTag(EmvTags.SIGNED_STATIC_APPLICATION_DATA)) {
                val startIndex = decodedSSD.startIndex + signedStaticData.startIndexOfValue
                val result = recoverText(signedStaticData.valueAsHexString, issuerPublicKeyCertificate, startIndex, ::decodeSignedStaticData)
                decodedSSD.addChildren(result.decoded)
                if (result.recoveredHex != null) {
                    decodedSSD.hexDump = HexDumpFactory().splitIntoByteLengthStrings(result.recoveredHex, startIndex)
                }
            }
        }
    }

}

fun decodeSignedStaticData(recovered: ByteArray, byteLengthOfIssuerModulus: Int, startIndexInBytes: Int): List<DecodedData> {
    return listOf(
            DecodedData.byteRange("Header", recovered, 0, 1, startIndexInBytes),
            DecodedData.byteRange("Format", recovered, 1, 1, startIndexInBytes),
            DecodedData.byteRange("Hash Algorithm", recovered, 2, 1, startIndexInBytes),
            DecodedData.byteRange("Data Auth Code", recovered, 3, 2, startIndexInBytes),
            DecodedData.byteRange("Hash", recovered, recovered.size-21, 20, startIndexInBytes),
            DecodedData.byteRange("Trailer", recovered, recovered.size-1, 1, startIndexInBytes)
    )
}

