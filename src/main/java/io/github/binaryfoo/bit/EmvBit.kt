package io.github.binaryfoo.bit

/**
 * EMV specs seem to follow the convention: bytes are numbered left to right, bits are numbered byte right to left,
 * both start at 1.
 */
data class EmvBit(val byteNumber: Int, val bitNumber: Int, val set: Boolean) : Comparable<EmvBit> {

    val value: String
    get() = if (set) "1" else "0"

    override fun toString(): String = toString(true)

    fun toString(includeComma: Boolean, includeValue: Boolean = true): String {
        val separator = if (includeComma) "," else ""
        if (includeValue) {
            return "Byte $byteNumber$separator Bit $bitNumber = $value"
        }
        return "Byte $byteNumber$separator Bit $bitNumber"
    }

    override fun compareTo(other: EmvBit): Int {
        val byteOrder = byteNumber.compareTo(other.byteNumber)
        if (byteOrder != 0) {
            return byteOrder
        }
        val bitOrder = other.bitNumber.compareTo(bitNumber)
        if (bitOrder != 0) {
            return bitOrder
        }
        return set.compareTo(other.set)
    }
}