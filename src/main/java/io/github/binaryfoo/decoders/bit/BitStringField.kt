package io.github.binaryfoo.decoders.bit

import io.github.binaryfoo.bit.EmvBit

interface BitStringField {
    fun getPositionIn(bits: Set<EmvBit>?): String
    fun getValueIn(bits: Set<EmvBit>): String?
    fun getStartBytesOffset(): Int
    fun getLengthInBytes(): Int
}
