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
            return toLabel(field, true)
        }
        return toHex(field, getByteCount(bits)) + " (" + toLabel(field, field.size() > 1) + ")"
    }

    override public fun getValueIn(bits: Set<EmvBit>): String? {
        if (intersects(field, bits)) {
            return value
        }
        return null
    }

    override fun getStartBytesOffset(): Int = field.first().byteNumber - 1

    override fun getLengthInBytes(): Int = 1

    private fun intersects(targetBits: Set<EmvBit>, bits: Set<EmvBit>): Boolean {
        val intersection = HashSet(targetBits)
        intersection.retainAll(bits)
        return intersection.size() == targetBits.size()
    }

    private fun toLabel(bits: Set<EmvBit>, includeValue: Boolean): String {
        val b = StringBuilder()
        for (bit in bits) {
            if (b.length() > 0) {
                b.append(", ")
            }
            if (includeValue) {
                b.append(bit.toLabel(false))
            } else {
                b.append("Byte ").append(bit.byteNumber).append(" Bit ").append(bit.bitNumber)
            }
        }
        return b.toString()
    }
}
