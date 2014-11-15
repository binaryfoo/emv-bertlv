package io.github.binaryfoo.decoders.bit

import io.github.binaryfoo.bit.*
import io.github.binaryfoo.bit.EmvBit

import java.util.HashSet
import java.util.TreeSet

/**
 * The english description attached to one or more bit positions being set (to 0 or 1) in a single byte.
 */
public class EnumeratedBitStringField(field: Set<EmvBit>, private val value: String) : BitStringField {

    private val field: Set<EmvBit>

    {
        this.field = TreeSet(field)
    }

    override public fun getPositionIn(bits: Set<EmvBit>?): String {
        if (bits == null) {
            return field.toString(includeValue = true)
        }
        return field.toHexString(bits.getByteCount()) + " (" + field.toString(field.size() > 1) + ")"
    }

    override public fun getValueIn(bits: Set<EmvBit>): String? {
        return if (field.matches(bits)) value else null
    }

    override fun getStartBytesOffset(): Int = field.first().byteNumber - 1

    override fun getLengthInBytes(): Int = 1
}
