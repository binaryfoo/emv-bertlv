package io.github.binaryfoo.bit;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Set;
import java.util.TreeSet;

/**
 * EMV specs seem to follow the convention: bytes are numbered left to right, bits are numbered byte right to left,
 * both start at 1.
 */
public class EmvBit implements Comparable<EmvBit> {
    private final int byteNumber;
    private final int bitNumber;
    private final boolean set;

    public EmvBit(int byteNumber, int bitNumber, boolean set) {
        this.byteNumber = byteNumber;
        this.bitNumber = bitNumber;
        this.set = set;
    }

    public int getByteNumber() {
        return byteNumber;
    }

    public int getBitNumber() {
        return bitNumber;
    }

    public boolean isSet() {
        return set;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return toLabel(true);
    }

    public String toLabel(boolean includeComma) {
        return String.format(includeComma ? "Byte %d, Bit %d = %d" : "Byte %d Bit %d = %d", byteNumber, bitNumber, set ? 1 : 0);
    }

    public static Set<EmvBit> fromHex(String hex) {
        Set<EmvBit> set = new TreeSet<>();
        for (int i = 0; i < hex.length(); i += 2) {
            int b = Integer.parseInt(hex.substring(i, i + 2), 16);
            int byteNumber = (i / 2) + 1;
            for (int j = 7; j >= 0; j--) {
                set.add(new EmvBit(byteNumber, j + 1, (b >> j & 0x1) == 1));
            }
        }
        return set;
    }

    @Override
    public int compareTo(EmvBit o) {
        int byteOrder = Integer.compare(byteNumber, o.byteNumber);
        if (byteOrder != 0) {
            return byteOrder;
        }
        int bitOrder = Integer.compare(o.bitNumber, bitNumber);
        if (bitOrder != 0) {
            return bitOrder;
        }
        return Boolean.compare(set, o.set);
    }
}
