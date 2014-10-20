package io.github.binaryfoo.decoders.bit

import io.github.binaryfoo.bit.EmvBit

import java.util.HashSet
import java.util.TreeSet

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
        if (intersects(field, bits)) {
            return decodedValue
        }
        return null
    }

    override fun getStartBytesOffset(): Int {
        return byteNumber - 1
    }

    override fun getLengthInBytes(): Int {
        return 1
    }

    private fun intersects(targetBits: Set<EmvBit>, bits: Set<EmvBit>): Boolean {
        val intersection = HashSet(targetBits)
        intersection.retainAll(bits)
        return intersection.size() == targetBits.size()
    }

}
