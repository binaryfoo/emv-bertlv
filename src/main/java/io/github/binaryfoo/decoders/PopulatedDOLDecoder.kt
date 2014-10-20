package io.github.binaryfoo.decoders

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.Decoder
import io.github.binaryfoo.TagInfo
import io.github.binaryfoo.TagMetaData
import io.github.binaryfoo.tlv.ISOUtil
import io.github.binaryfoo.tlv.Tag

import java.nio.ByteBuffer
import java.util.ArrayList

public class PopulatedDOLDecoder : Decoder {
    override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): MutableList<DecodedData> {
        val fields = input.split(":")
        val pdol = fields[0]
        val populatedPDOL = fields[1]
        return decode(pdol, populatedPDOL, pdol.length() / 2, session)
    }

    public fun decode(pdol: String, populatedPDOL: String, startIndexInBytes: Int, session: DecodeSession): MutableList<DecodedData> {
        val decoded = ArrayList<DecodedData>()
        val values = ByteBuffer.wrap(ISOUtil.hex2byte(populatedPDOL))
        val elements = DOLParser().parse(ISOUtil.hex2byte(pdol))
        var offset = startIndexInBytes
        for (element in elements) {
            val value = ByteArray(element.length)
            values.get(value)
            val tagMetaData = session.getTagMetaData()
            val tag = element.tag
            val tagInfo = tagMetaData!!.get(tag)
            val valueAsHexString = ISOUtil.hexString(value)
            val children = tagInfo.decoder.decode(valueAsHexString, offset, session)
            val decodedData = DecodedData.constructed(tag.toString(tagMetaData), tagInfo.decodePrimitiveTlvValue(valueAsHexString), offset, offset + value.size, children)
            decoded.add(decodedData)
            offset += value.size
        }
        return decoded
    }

    override fun validate(input: String?): String? {
        val fields = input?.split(":")?.size ?: 0
        if (fields != 2) {
            return "Put : between the DOL and the populated list"
        }
        return null
    }

    override fun getMaxLength(): Int {
        return Integer.MAX_VALUE
    }
}
