package io.github.binaryfoo.decoders

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.Decoder
import io.github.binaryfoo.EmvTags
import io.github.binaryfoo.TagMetaData
import io.github.binaryfoo.decoders.apdu.APDUCommand
import io.github.binaryfoo.tlv.Tag

import java.util.Arrays

public class ResponseFormat1Decoder : Decoder {
    override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): List<DecodedData> {
        if (session.currentCommand == APDUCommand.GetProcessingOptions) {
            val aip = input.substring(0, 4)
            val afl = input.substring(4)
            return listOf(
                    decode(EmvTags.APPLICATION_INTERCHANGE_PROFILE, aip, startIndexInBytes, 2, session),
                    decode(EmvTags.AFL, afl, startIndexInBytes + 2, (input.length() - 4) / 2, session))
        }
        if (session.currentCommand == APDUCommand.GenerateAC) {
            val cid = input.substring(0, 2)
            val atc = input.substring(2, 6)
            val applicationCryptogram = input.substring(6, 22)
            val issuerApplicationData = input.substring(22)
            return listOf(
                    decode(EmvTags.CRYPTOGRAM_INFORMATION_DATA, cid, startIndexInBytes, 1, session),
                    decode(EmvTags.APPLICATION_TRANSACTION_COUNTER, atc, startIndexInBytes + 1, 2, session),
                    decode(EmvTags.APPLICATION_CRYPTOGRAM, applicationCryptogram, startIndexInBytes + 3, 8, session),
                    decode(EmvTags.ISSUER_APPLICATION_DATA, issuerApplicationData, startIndexInBytes + 11, (input.length() - 22) / 2, session))
        }
        if (session.currentCommand == APDUCommand.InternalAuthenticate) {
            // 9F4B is only used for Format 2 responses to Internal authenticate
            session.signedDynamicAppData = input
        }
        return listOf()
    }

    private fun decode(tag: Tag, value: String, startIndexInBytes: Int, length: Int, decodeSession: DecodeSession): DecodedData {
        val tagMetaData = decodeSession.tagMetaData!!
        val children = tagMetaData.get(tag).decoder.decode(value, startIndexInBytes, decodeSession)
        return DecodedData.fromTlv(tag, tag.toString(tagMetaData), tagMetaData.get(tag).decodePrimitiveTlvValue(value), startIndexInBytes, startIndexInBytes + length, children)
    }

    override fun validate(input: String?): String? {
        return null
    }

    override fun getMaxLength(): Int {
        return 0
    }
}
