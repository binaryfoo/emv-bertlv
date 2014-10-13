package io.github.binaryfoo.decoders.bit;

import io.github.binaryfoo.bit.BitPackage;
import io.github.binaryfoo.bit.EmvBit;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class EnumeratedBitStringField implements BitStringField {

    private final Set<EmvBit> field;
    private final String value;

    public EnumeratedBitStringField(Set<EmvBit> field, String value) {
        this.field = new TreeSet<>(field);
        this.value = value;
    }

    @Override
    public String getPositionIn(Set<EmvBit> bitString) {
        if (bitString == null) {
            return toLabel(field, true);
        }
        return BitPackage.toHex(field, BitPackage.getByteCount(bitString)) + " (" + toLabel(field, field.size() > 1) + ")";
    }

    @Override
    public String getValueIn(Set<EmvBit> bitString) {
        if (intersects(field, bitString)) {
            return value;
        }
        return null;
    }

    @Override
    public int getStartBytesOffset() {
        return field.iterator().next().getByteNumber() - 1;
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

    private String toLabel(Set<EmvBit> bits, boolean includeValue) {
        StringBuilder b = new StringBuilder();
        for (EmvBit bit : bits) {
            if (b.length() > 0) {
                b.append(", ");
            }
            if (includeValue) {
                b.append(bit.toLabel(false));
            } else {
                b.append("Byte ").append(bit.getByteNumber()).append(" Bit ").append(bit.getBitNumber());
            }
        }
        return b.toString();
    }
}
