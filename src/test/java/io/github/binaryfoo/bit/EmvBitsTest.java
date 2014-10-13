package io.github.binaryfoo.bit;

import org.junit.Test;

import java.util.Set;

import static io.github.binaryfoo.bit.BitPackage.getByteCount;
import static io.github.binaryfoo.bit.BitPackage.setOf;
import static io.github.binaryfoo.bit.BitPackage.toConfigString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class EmvBitsTest {

    @Test
    public void bitsToConfigString() throws Exception {
        Set<EmvBit> bits = setOf(new EmvBit(1, 8, true), new EmvBit(1, 1, false));
        assertThat(toConfigString(bits), is("(1,8)=1 & (1,1)=0"));
    }

    @Test
    public void bitToConfigString() throws Exception {
        assertThat(toConfigString(new EmvBit(2, 5, true)), is("(2,5)=1"));
    }

    @Test
    public void count() throws Exception {
        assertThat(getByteCount(BitPackage.fromHex("01")), is(1));
        assertThat(getByteCount(BitPackage.fromHex("0000")), is(2));
    }
}