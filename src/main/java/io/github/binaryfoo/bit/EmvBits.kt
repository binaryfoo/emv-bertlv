@file:JvmName("BitPackage")
package io.github.binaryfoo.bit

import java.util.TreeSet
import io.github.binaryfoo.tlv.ISOUtil
import kotlin.collections.*
import kotlin.ranges.downTo
import kotlin.ranges.step
import kotlin.text.substring


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

public fun Set<EmvBit>.toHexString(fieldLengthInBytes: Int): String {
    val bytes = ByteArray(fieldLengthInBytes)
    for (bit in this) {
        if (bit.set) {
            val byteIndex = bit.byteNumber - 1
            var b = bytes[byteIndex]
            b = (b.toInt() or (1 shl bit.bitNumber - 1)).toByte()
            bytes[byteIndex] = b
        }
    }
    return ISOUtil.hexString(bytes)
}

public fun Set<EmvBit>.reduceToOnBits(): Set<EmvBit> = TreeSet(filter { it.set })

public fun setOf(vararg bits: EmvBit): Set<EmvBit> = sortedSetOf(*bits)

public fun Set<EmvBit>.toConfigString(): String {
    val b = StringBuilder()
    for (bit in this) {
        if (b.length > 0) {
            b.append(" & ")
        }
        b.append(bit.toConfigString())
    }
    return b.toString()
}

public fun EmvBit.toConfigString(): String {
    return "($byteNumber,$bitNumber)=$value"
}

public fun Set<EmvBit>.getByteCount(): Int {
    return map { it.byteNumber }.reduce { a, b -> if (a >= b) a else b }
}

public fun Set<EmvBit>.matches(other: Set<EmvBit>): Boolean {
    return intersect(other).size == size
}

public fun Set<EmvBit>.toString(includeValue: Boolean): String {
    val b = StringBuilder()
    for (bit in this) {
        if (b.length > 0) {
            b.append(", ")
        }
        b.append(bit.toString(false, includeValue))
    }
    return b.toString()
}

/**
 * Label set bits (those = 1) in hex.
 */
public fun labelFor(hex: String): String {
    return fromHex(hex).toString(includeValue = false)
}