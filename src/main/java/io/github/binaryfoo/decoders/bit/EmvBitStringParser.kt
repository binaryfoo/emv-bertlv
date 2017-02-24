package io.github.binaryfoo.decoders.bit

import io.github.binaryfoo.bit.EmvBit
import io.github.binaryfoo.bit.fromHex
import org.apache.commons.lang.StringUtils
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.filter
import kotlin.collections.map
import kotlin.text.*

object EmvBitStringParser {
    val SINGLE_BIT_PATTERN = Pattern.compile("\\s*\\((\\d+),(\\d+)\\)=(\\d+)\\s*")
    val NUMERIC_FIELD_PATTERN = Pattern.compile("\\s*\\((\\d+),(\\d+)-(\\d+)\\)=INT\\s*")
    val FULL_BYTE_FIELD_PATTERN = Pattern.compile("\\s*\\((\\d+)\\)=0x([0-9a-fA-F]{2})\\s*")

    @JvmStatic fun parse(lines: List<String>): List<BitStringField> {
        fun usefulLine(line: String) = StringUtils.isNotBlank(line) && !line.startsWith("#")

        return lines.filter(::usefulLine).map {
            val fields = it.split(Regex("\\s*:\\s*"), 2)
            parseField(fields[0], fields[1])
        }
    }

    private fun parseField(key: String, label: String): BitStringField {
        if (key.contains("-")) {
            return parseNumericField(key, label)
        } else if (key.contains(",")) {
            return parseEnumeratedField(key, label)
        } else {
            return parseFullByteField(key, label)
        }
    }

    private fun parseNumericField(key: String, label: String): BitStringField {
        val matcher = match(key, NUMERIC_FIELD_PATTERN, "numeric")
        val byteNumber = matcher.group(1).toInt()
        val firstBit = matcher.group(2).toInt()
        val lastBit = matcher.group(3).toInt()
        return NumericBitStringField(byteNumber, firstBit, lastBit, label)
    }

    private fun parseEnumeratedField(key: String, label: String): BitStringField {
        val bits = TreeSet(key.split("&").map(::parseBit))
        return EnumeratedBitStringField(bits, label)
    }

    private fun parseFullByteField(key: String, label: String): BitStringField {
        val matcher = match(key, FULL_BYTE_FIELD_PATTERN, "full byte")
        val byteNumber = matcher.group(1).toInt()
        val hexValue = matcher.group(2)
        val bits = fromHex(hexValue, byteNumber)
        return FullByteField(bits, byteNumber, hexValue, label)
    }
}

private fun parseBit(key: String): EmvBit {
    val matcher = match(key, EmvBitStringParser.SINGLE_BIT_PATTERN, "enumerated")
    val byteNumber = matcher.group(1).toInt()
    val bitNumber = matcher.group(2).toInt()
    val bitValue = matcher.group(3) == "1"
    return EmvBit(byteNumber, bitNumber, bitValue)
}

private fun match(key: String, pattern: Pattern, name: String): Matcher {
    val matcher = pattern.matcher(key)
    if (!matcher.matches()) {
        throw IllegalArgumentException("Not a valid $name field mapping [$key]")
    }
    return matcher
}
