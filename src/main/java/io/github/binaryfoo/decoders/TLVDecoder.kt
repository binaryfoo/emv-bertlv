package io.github.binaryfoo.decoders

import java.util.ArrayList

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.Decoder
import io.github.binaryfoo.TagInfo
import io.github.binaryfoo.TagMetaData
import io.github.binaryfoo.tlv.BerTlv
import io.github.binaryfoo.tlv.ISOUtil
import io.github.binaryfoo.tlv.Tag

public class TLVDecoder : Decoder {

    override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): List<DecodedData> {
        val list = BerTlv.parseList(ISOUtil.hex2byte(input), true)
        return decodeTlvs(list, startIndexInBytes, session)
    }

    private fun decodeTlvs(list: List<BerTlv>, startIndex: Int, session: DecodeSession): List<DecodedData> {
        var currentStartIndex = startIndex
        val decodedItems = ArrayList<DecodedData>()
        for (berTlv in list) {
            val valueAsHexString = berTlv.getValueAsHexString()
            val tag = berTlv.tag
            val length = berTlv.toBinary().size
            val contentEndIndex = currentStartIndex + length
            val compositeStartElementIndex = currentStartIndex + tag.bytes.size + berTlv.getLengthInBytesOfEncodedLength()
            val tagMetaData = session.tagMetaData!!
            if (tag.isConstructed()) {
                decodedItems.add(DecodedData.fromTlv(tag, tag.toString(tagMetaData), valueAsHexString, currentStartIndex, contentEndIndex, decodeTlvs(berTlv.getChildren(), compositeStartElementIndex, session)))
            } else {
                val tagInfo = tagMetaData.get(tag)
                decodedItems.add(DecodedData.fromTlv(tag, tag.toString(tagMetaData), tagInfo.decodePrimitiveTlvValue(valueAsHexString), currentStartIndex, contentEndIndex, tagInfo.decoder.decode(valueAsHexString, compositeStartElementIndex, session)))
            }
            currentStartIndex += length
        }
        session.rememberTags(list)
        return decodedItems
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
