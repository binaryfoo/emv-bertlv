package io.github.binaryfoo.bit

import java.util.Arrays
import java.util.TreeSet

public fun reduceToOnBits(emvBits: Set<EmvBit>): Set<EmvBit> {
    return TreeSet(emvBits.filter { it.set })
}

public fun setOf(vararg bits: EmvBit): Set<EmvBit> {
    return sortedSetOf(*bits)
}

public fun toConfigString(bits: Set<EmvBit>): String {
    val b = StringBuilder()
    for (bit in bits) {
        if (b.length() > 0) {
            b.append(" & ")
        }
        b.append(toConfigString(bit))
    }
    return b.toString()
}

public fun toConfigString(b: EmvBit): String {
    return "(${b.byteNumber},${b.bitNumber})=${b.value}"
}

public fun getByteCount(bitString: Set<EmvBit>): Int {
    var count = 0
    for (bit in bitString) {
        count = Math.max(count, bit.byteNumber)
    }
    return count
}