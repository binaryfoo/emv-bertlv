package io.github.binaryfoo.decoders.bit;

import io.github.binaryfoo.bit.EmvBit;

import java.util.BitSet;
import java.util.Set;

public class NumericBitStringField implements BitStringField {

    private final int byteNumber;
    private final int firstBit;
    private final int lastBit;
    private final String name;

    public NumericBitStringField(int byteNumber, int firstBit, int lastBit, String name) {
        if (lastBit > firstBit) {
            throw new IllegalArgumentException("Must be left to right order: " + lastBit + " > " + firstBit);
        }
        this.byteNumber = byteNumber;
        this.firstBit = firstBit;
        this.lastBit = lastBit;
        this.name = name;
    }

    @Override
    public String getPositionDescription() {
        return String.format("Byte %d Bits %d-%d", byteNumber, firstBit, lastBit);
    }

    @Override
    public String getValueIn(Set<EmvBit> bitstring) {
        BitSet theByte = new BitSet(8);
        for (EmvBit bit : bitstring) {
            if (bit.getByteNumber() == byteNumber && bit.getBitNumber() <= firstBit && bit.getBitNumber() >= lastBit) {
                theByte.set(bit.getBitNumber() - 1, bit.isSet());
            }
        }
        byte[] bytes = theByte.toByteArray();
        int i = bytes.length == 0 ? 0 : (bytes[0] & 0x000000ff) >>> (lastBit - 1);
        return name + " = " + String.valueOf(i);
    }

    @Override
    public int getStartBytesOffset() {
        return byteNumber - 1;
    }

    @Override
    public int getLengthInBytes() {
        return 1;
    }
}
