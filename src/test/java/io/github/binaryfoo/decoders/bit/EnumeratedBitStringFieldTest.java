package io.github.binaryfoo.decoders.bit;

import io.github.binaryfoo.bit.EmvBit;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

public class EnumeratedBitStringFieldTest {

    @Test
    public void twoBits() throws Exception {
        EnumeratedBitStringField field = new EnumeratedBitStringField(new HashSet<>(Arrays.asList(new EmvBit(3, 8, true), new EmvBit(3, 7, false))), "V1");
        assertThat(field.getValueIn(EmvBit.fromHex("000080")), is("V1"));
        assertThat(field.getValueIn(EmvBit.fromHex("0000C0")), is(nullValue()));
        assertThat(field.getPositionDescription(), is("Byte 3 Bit 8 = 1, Byte 3 Bit 7 = 0"));
        assertThat(field.getStartBytesOffset(), is(2));
        assertThat(field.getLengthInBytes(), is(1));
    }
}