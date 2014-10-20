package io.github.binaryfoo.decoders.bit

import io.github.binaryfoo.bit.EmvBit

public trait BitStringField {
    public fun getPositionIn(bits: Set<EmvBit>?): String
    public fun getValueIn(bits: Set<EmvBit>): String?
    public fun getStartBytesOffset(): Int
    public fun getLengthInBytes(): Int
}
