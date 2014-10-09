package io.github.binaryfoo.decoders.bit;

import io.github.binaryfoo.bit.EmvBit;

import java.util.Set;

public interface BitStringField {
    String getPositionDescription();
    String getValueIn(Set<EmvBit> bitstring);
    int getStartBytesOffset();
    int getLengthInBytes();
}
