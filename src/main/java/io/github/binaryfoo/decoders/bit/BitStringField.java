package io.github.binaryfoo.decoders.bit;

import io.github.binaryfoo.bit.EmvBit;

import java.util.Set;

public interface BitStringField {
    String getPositionIn(Set<EmvBit> bits);
    String getValueIn(Set<EmvBit> bits);
    int getStartBytesOffset();
    int getLengthInBytes();
}
