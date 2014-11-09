package io.github.binaryfoo.decoders.bit

import io.github.binaryfoo.bit.*
import io.github.binaryfoo.bit.EmvBit
import org.apache.commons.lang.StringUtils

import java.io.IOException
import java.util.ArrayList
import java.util.TreeSet
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.platform.platformStatic

public class EmvBitStringParser {
    class object {

        private val ENUMERATED_FIELD_PATTERN = Pattern.compile("\\s*\\((\\d+),(\\d+)\\)=(\\d+)\\s*")
        private val NUMERIC_FIELD_PATTERN = Pattern.compile("\\s*\\((\\d+),(\\d+)-(\\d+)\\)=INT\\s*")
        private val FULL_BYTE_FIELD_PATTERN = Pattern.compile("\\s*\\((\\d+)\\)=0x([0-9a-fA-F]{2})\\s*")

        platformStatic public fun parse(lines: List<String>): List<BitStringField> {
            val bitMappings = ArrayList<BitStringField>()
            for (line in lines) {
                if (StringUtils.isNotBlank(line) && !line.startsWith("#")) {
                    val fields = line.split("\\s*:\\s*", 2)
                    bitMappings.add(parseField(fields[0], fields[1]))
                }
            }
            return bitMappings
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
            val matcher = NUMERIC_FIELD_PATTERN.matcher(key)
            if (!matcher.matches()) {
                throw IllegalArgumentException("Not a valid numeric field mapping [" + key + "]")
            }
            val byteNumber = Integer.parseInt(matcher.group(1))
            val firstBit = Integer.parseInt(matcher.group(2))
            val lastBit = Integer.parseInt(matcher.group(3))
            return NumericBitStringField(byteNumber, firstBit, lastBit, label)
        }

        private fun parseEnumeratedField(key: String, label: String): BitStringField {
            val bits = TreeSet<EmvBit>()
            for (bit in key.split("&")) {
                bits.add(parseBit(bit))
            }
            return EnumeratedBitStringField(bits, label)
        }

        private fun parseBit(key: String): EmvBit {
            val matcher = ENUMERATED_FIELD_PATTERN.matcher(key)
            if (!matcher.matches()) {
                throw IllegalArgumentException("Not a valid enumerated field mapping [" + key + "]")
            }
            val byteNumber = Integer.parseInt(matcher.group(1))
            val bitNumber = Integer.parseInt(matcher.group(2))
            val bitValue = matcher.group(3) == "1"
            return EmvBit(byteNumber, bitNumber, bitValue)
        }

        private fun parseFullByteField(key: String, label: String): BitStringField {
            val matcher = FULL_BYTE_FIELD_PATTERN.matcher(key)
            if (!matcher.matches()) {
                throw IllegalArgumentException("Not a valid full byte field mapping [" + key + "]")
            }
            val byteNumber = Integer.parseInt(matcher.group(1))
            val hexValue = matcher.group(2)
            val bits = fromHex(hexValue, byteNumber)
            return FullByteField(bits, byteNumber, hexValue, label)
        }
    }
}
