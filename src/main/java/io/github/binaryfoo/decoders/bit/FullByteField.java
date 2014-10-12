package io.github.binaryfoo.decoders.bit;

import io.github.binaryfoo.bit.EmvBit;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class FullByteField implements BitStringField {

    private final Set<EmvBit> field;
    private final int byteNumber;
    private final String hexValue;
    private final String decodedValue;

    public FullByteField(Set<EmvBit> field, int byteNumber, String hexValue, String decodedValue) {
        this.byteNumber = byteNumber;
        this.hexValue = hexValue;
        this.field = new TreeSet<>(field);
        this.decodedValue = decodedValue;
    }

    @Override
    public String getPositionIn(Set<EmvBit> bitString) {
        return String.format("Byte %d = 0x%s", byteNumber, hexValue);
    }

    @Override
    public String getValueIn(Set<EmvBit> bitString) {
        if (intersects(field, bitString)) {
            return decodedValue;
        }
        return null;
    }

    @Override
    public int getStartBytesOffset() {
        return byteNumber - 1;
    }

    @Override
    public int getLengthInBytes() {
        return 1;
    }

    private boolean intersects(Set<EmvBit> targetBits, Set<EmvBit> bits) {
        Set<EmvBit> intersection = new HashSet<>(targetBits);
        intersection.retainAll(bits);
        return intersection.size() == targetBits.size();
    }

}
