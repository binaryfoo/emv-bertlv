package io.github.binaryfoo.decoders

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.Decoder
import io.github.binaryfoo.bit.fromHex
import io.github.binaryfoo.decoders.bit.BitStringField
import io.github.binaryfoo.decoders.bit.EmvBitStringParser
import io.github.binaryfoo.res.ClasspathIO
import io.github.binaryfoo.tlv.ISOUtil
import org.apache.commons.io.IOUtils
import java.io.IOException
import java.util.*

/**
 * Decoder based on the config language (DSL...).
 * <p>
 * Each line in fileName should be parseable in one of the 3 formats:
 * <ul>
 *     <li>Enumerated - a direct mapping from a set of bits to a name. Bits can be spread over multiple bytes.</li>
 *     <li>Full byte - similar to enumerated but only handles a single byte.</li>
 *     <li>Numeric - the left or right nibble in a byte is interpreted as an integer.</li>
 * </ul>
 */
open class EmvBitStringDecoder(fileName: String, val showFieldHexInDecoding: Boolean) : Decoder {

    private val bitMappings: List<BitStringField>
    private val maxLength: Int

    init {
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
        if (bitMappings.isEmpty()) {
            return 0
        }
        fun max(a: Int, b: Int): Int = if ((a >= b)) a else b
        return bitMappings.map { it.getStartBytesOffset() + it.getLengthInBytes() }.reduce(::max)
    }

    override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): List<DecodedData> {
        val decoded = ArrayList<DecodedData>()
        val bits = fromHex(input)
        for (field in bitMappings) {
            val v = field.getValueIn(bits)
            if (v != null) {
                val fieldStartIndex = startIndexInBytes + field.getStartBytesOffset()
                decoded.add(DecodedData.primitive(field.getPositionIn(if (showFieldHexInDecoding) bits else null), v, fieldStartIndex, fieldStartIndex + field.getLengthInBytes()))
            }
        }
        return decoded
    }

    override fun validate(input: String?): String? {
        if (input == null || input.length != maxLength) {
            return "Value must be exactly $maxLength characters"
        }
        if (!ISOUtil.isValidHexString(input)) {
            return "Value must contain only the characters 0-9 and A-F"
        }
        return null
    }

}
