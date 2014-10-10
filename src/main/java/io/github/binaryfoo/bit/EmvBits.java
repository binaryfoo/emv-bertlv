package io.github.binaryfoo.bit;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class EmvBits {
    public static Set<EmvBit> reduceToOnBits(Set<EmvBit> emvBits) {
        Set<EmvBit> setBits = new TreeSet<>();
        for (EmvBit bit : emvBits) {
            if (bit.isSet()) {
                setBits.add(bit);
            }
        }
        return setBits;
    }

    public static Set<EmvBit> setOf(EmvBit... bits) {
        return new TreeSet<>(Arrays.asList(bits));
    }

    public static String toConfigString(Set<EmvBit> bits) {
        StringBuilder b = new StringBuilder();
        for (EmvBit bit : bits) {
            if (b.length() > 0) {
                b.append(" & ");
            }
            b.append(toConfigString(bit));
        }
        return b.toString();
    }

    public static String toConfigString(EmvBit b) {
        return String.format("(%d,%d)=%d", b.getByteNumber(), b.getBitNumber(), b.isSet() ? 1 : 0);
    }

    public static int getByteCount(Set<EmvBit> bitString) {
        int count = 0;
        for (EmvBit bit : bitString) {
            count = Math.max(count, bit.getByteNumber());
        }
        return count;
    }
}
