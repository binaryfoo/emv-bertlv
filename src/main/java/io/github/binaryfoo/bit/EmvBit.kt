package io.github.binaryfoo.bit

import io.github.binaryfoo.tlv.ISOUtil
import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder
import java.util.TreeSet

/**
 * EMV specs seem to follow the convention: bytes are numbered left to right, bits are numbered byte right to left,
 * both start at 1.
 */
public data class EmvBit(public val byteNumber: Int, public val bitNumber: Int, public val set: Boolean) : Comparable<EmvBit> {

    public val value: String
    get() = if (set) "1" else "0"

    override fun toString(): String = toLabel(true)

    public fun toLabel(includeComma: Boolean): String {
        val separator = if (includeComma) "," else ""
        return "Byte ${byteNumber}${separator} Bit ${bitNumber} = ${value}"
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

// java interop
public fun fromHex(hex: String): Set<EmvBit> = fromHex(hex, 1)

public fun fromHex(hex: String, firstByteNumber: Int): Set<EmvBit> {
    val set = TreeSet<EmvBit>()
    for (i in 0..hex.length-1 step 2) {
        val b = Integer.parseInt(hex.substring(i, i + 2), 16)
        val byteNumber = (i / 2) + firstByteNumber
        for (j in 7 downTo 0) {
            set.add(EmvBit(byteNumber, j + 1, (b shr j and 1) == 1))
        }
    }
    return set
}

public fun toHex(bits: Set<EmvBit>, fieldLengthInBytes: Int): String {
    val bytes = ByteArray(fieldLengthInBytes)
    for (bit in bits) {
        if (bit.set) {
            val byteIndex = bit.byteNumber - 1
            var b = bytes[byteIndex]
            b = (b.toInt() or (1 shl bit.bitNumber - 1)).toByte()
            bytes[byteIndex] = b
        }
    }
    return ISOUtil.hexString(bytes)
}


/**
 * Label set bits (those = 1) in hex.
 */
public fun labelFor(hex: String): String {
    val label = StringBuilder()
    for (bit in fromHex(hex)) {
        if (bit.set) {
            if (label.length() > 0) {
                label.append(",")
            }
            label.append("Byte ").append(bit.byteNumber).append(" Bit ").append(bit.bitNumber)
        }
    }
    return label.toString()
}
