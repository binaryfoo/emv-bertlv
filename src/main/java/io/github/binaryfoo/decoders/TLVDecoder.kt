package io.github.binaryfoo.decoders

import java.util.ArrayList

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.Decoder
import io.github.binaryfoo.TagInfo
import io.github.binaryfoo.TagMetaData
import io.github.binaryfoo.tlv.BerTlv
import io.github.binaryfoo.tlv.ISOUtil
import io.github.binaryfoo.tlv.Tag
import io.github.binaryfoo.tlv.TlvParseException

public class TLVDecoder : Decoder {

    override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): List<DecodedData> {
        val list: List<BerTlv>
        try {
            list = BerTlv.parseList(ISOUtil.hex2byte(input), true)
            return decodeTlvs(list, startIndexInBytes, session)
        } catch(e: TlvParseException) {
            val decoded = decodeTlvs(e.resultsSoFar, startIndexInBytes, session)
            val error = DecodedData(null, "Error", e.getMessage()?:e.javaClass.getSimpleName(), decoded.last?.endIndex ?: 0, input.length / 2, category = "parse-error")
            return decoded + error
        }
    }

    private fun decodeTlvs(list: List<BerTlv>, startIndex: Int, session: DecodeSession): List<DecodedData> {
        var currentStartIndex = startIndex
        val decodedItems = ArrayList<DecodedData>()
        for (tlv in list) {
            val valueAsHexString = tlv.valueAsHexString
            val tag = tlv.tag
            val length = tlv.toBinary().size
            val contentEndIndex = currentStartIndex + length
            val compositeStartElementIndex = currentStartIndex + tlv.startIndexOfValue
            val tagMetaData = session.tagMetaData!!
            val decoded = if (tag.constructed) {
                DecodedData.fromTlv(tlv, tagMetaData, valueAsHexString, currentStartIndex, contentEndIndex, decodeTlvs(tlv.getChildren(), compositeStartElementIndex, session))
            } else {
                val tagInfo = tagMetaData.get(tag)
                DecodedData.fromTlv(tlv, tagMetaData, tagInfo.decodePrimitiveTlvValue(valueAsHexString), currentStartIndex, contentEndIndex, decodeOrBackDown(compositeStartElementIndex, tagInfo, valueAsHexString, session))
            }
            decodedItems.add(decoded)
            currentStartIndex += length
        }
        return decodedItems
    }

    private fun decodeOrBackDown(compositeStartElementIndex: Int, tagInfo: TagInfo, valueAsHexString: String, session: DecodeSession): List<DecodedData> {
        try {
            return tagInfo.decoder.decode(valueAsHexString, compositeStartElementIndex, session)
        } catch(e: Exception) {
            return listOf(DecodedData(null, "Error: Failed parsing " + valueAsHexString, e.getMessage()?:e.javaClass.getSimpleName(), compositeStartElementIndex, compositeStartElementIndex + valueAsHexString.size/2, category = "parse-error"))
        }
    }

    override fun getMaxLength(): Int {
        return 10000
    }

    override fun validate(input: String?): String? {
        if (input == null || input.length() < 2) {
            return "Value must be at least 2 characters"
        }
        if (input.length() % 2 != 0) {
            return "Length must be a multiple of 2"
        }
        if (!ISOUtil.isValidHexString(input)) {
            return "Value must contain only the characters 0-9 and A-F"
        }
        return null
    }

}
