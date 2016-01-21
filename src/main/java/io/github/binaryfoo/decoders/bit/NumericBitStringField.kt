package io.github.binaryfoo.decoders.bit

import io.github.binaryfoo.bit.EmvBit

import java.util.BitSet

/**
 * An integer masked out of a number of bits in a single byte.
 */
public class NumericBitStringField(private val byteNumber: Int, private val firstBit: Int, private val lastBit: Int, private val name: String) : BitStringField {

    init {
        if (lastBit > firstBit) {
            throw IllegalArgumentException("Must be left to right order: $lastBit > $firstBit")
        }
    }

    override public fun getPositionIn(bits: Set<EmvBit>?): String {
        return "Byte $byteNumber Bits $firstBit-$lastBit"
    }

    override public fun getValueIn(bits: Set<EmvBit>): String {
        val theByte = BitSet(8)
        for (bit in bits) {
            if (bit.byteNumber == byteNumber && bit.bitNumber <= firstBit && bit.bitNumber >= lastBit) {
                theByte.set(bit.bitNumber - 1, bit.set)
            }
        }
        val bytes = theByte.toByteArray()
        val i = if (bytes.size == 0) 0 else (bytes[0].toInt() and 255).ushr((lastBit - 1))
        return "$name = $i"
    }

    override fun getStartBytesOffset(): Int = byteNumber - 1

    override fun getLengthInBytes(): Int  = 1
}
