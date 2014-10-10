package io.github.binaryfoo.decoders.bit;

import io.github.binaryfoo.bit.EmvBit;
import io.github.binaryfoo.bit.EmvBits;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

public class EnumeratedBitStringFieldTest {

    @Test
    public void positionInProvidesOptionalHexString() throws Exception {
        EnumeratedBitStringField field = new EnumeratedBitStringField(EmvBits.setOf(new EmvBit(3, 8, true)), "V1");
        assertThat(field.getPositionIn(null), is("Byte 3 Bit 8 = 1"));
        assertThat(field.getPositionIn(EmvBit.fromHex("000000")), is("000080 (Byte 3 Bit 8)"));
    }

    @Test
    public void hexStringInPositionIncludesValuesForTwoBitField() throws Exception {
        EnumeratedBitStringField field = new EnumeratedBitStringField(EmvBits.setOf(new EmvBit(2, 8, true), new EmvBit(2, 6, true)), "V1");
        assertThat(field.getPositionIn(EmvBit.fromHex("000000")), is("00A000 (Byte 2 Bit 8 = 1, Byte 2 Bit 6 = 1)"));
    }

    @Test
    public void twoBits() throws Exception {
        EnumeratedBitStringField field = new EnumeratedBitStringField(EmvBits.setOf(new EmvBit(3, 8, true), new EmvBit(3, 7, false)), "V1");
        assertThat(field.getValueIn(EmvBit.fromHex("000080")), is("V1"));
        assertThat(field.getValueIn(EmvBit.fromHex("0000C0")), is(nullValue()));
        assertThat(field.getPositionIn(null), is("Byte 3 Bit 8 = 1, Byte 3 Bit 7 = 0"));
        assertThat(field.getStartBytesOffset(), is(2));
        assertThat(field.getLengthInBytes(), is(1));
    }
}