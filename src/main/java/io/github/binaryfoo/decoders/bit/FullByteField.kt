package io.github.binaryfoo.decoders.bit

import io.github.binaryfoo.bit.EmvBit

import java.util.HashSet
import java.util.TreeSet
import io.github.binaryfoo.bit.matches

/**
 * An english description for a hex literal of a single byte.
 */
public class FullByteField(field: Set<EmvBit>, private val byteNumber: Int, private val hexValue: String, private val decodedValue: String) : BitStringField {

    private val field: Set<EmvBit>

    {
        this.field = TreeSet(field)
    }

    override public fun getPositionIn(bits: Set<EmvBit>?): String {
        return "Byte ${byteNumber} = 0x${hexValue}"
    }

    override public fun getValueIn(bits: Set<EmvBit>): String? {
        return if (field.matches(bits)) decodedValue else null
    }

    override fun getStartBytesOffset(): Int = byteNumber - 1

    override fun getLengthInBytes(): Int = 1

}
