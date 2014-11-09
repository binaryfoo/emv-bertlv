package io.github.binaryfoo.decoders

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.Decoder
import io.github.binaryfoo.bit.*
import io.github.binaryfoo.bit.EmvBit
import io.github.binaryfoo.decoders.bit.BitStringField
import io.github.binaryfoo.decoders.bit.EmvBitStringParser
import io.github.binaryfoo.res.ClasspathIO
import io.github.binaryfoo.tlv.ISOUtil
import org.apache.commons.io.IOUtils

import java.io.IOException
import java.io.InputStream
import java.util.ArrayList

/**
 * Build a decoder based on the config language (DSL...).
 */
open public class EmvBitStringDecoder(fileName: String, val showFieldHexInDecode: Boolean) : Decoder {

    private val bitMappings: List<BitStringField>
    private val maxLength: Int

    {
        val input = ClasspathIO.open(fileName)
        try {
            bitMappings = EmvBitStringParser.parse(IOUtils.readLines(input))
        } catch (e: IOException) {
            throw RuntimeException(e)
        } finally {
            IOUtils.closeQuietly(input)
        }
        this.maxLength = findMaxLengthInBytes() * 2
    }

    override fun getMaxLength(): Int = maxLength

    private fun findMaxLengthInBytes(): Int {
        var maxLength = 0
        for (mapping in bitMappings) {
            maxLength = Math.max(mapping.getStartBytesOffset() + mapping.getLengthInBytes(), maxLength)
        }
        return maxLength
    }

    override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): List<DecodedData> {
        val decoded = ArrayList<DecodedData>()
        val bits = fromHex(input)
        for (field in bitMappings) {
            val v = field.getValueIn(bits)
            if (v != null) {
                val fieldStartIndex = startIndexInBytes + field.getStartBytesOffset()
                decoded.add(DecodedData.primitive(field.getPositionIn(if (showFieldHexInDecode) bits else null), v, fieldStartIndex, fieldStartIndex + field.getLengthInBytes()))
            }
        }
        return decoded
    }

    override fun validate(input: String?): String? {
        if (input == null || input.length() != maxLength) {
            return "Value must be exactly ${maxLength} characters"
        }
        if (!ISOUtil.isValidHexString(input)) {
            return "Value must contain only the characters 0-9 and A-F"
        }
        return null
    }

}
