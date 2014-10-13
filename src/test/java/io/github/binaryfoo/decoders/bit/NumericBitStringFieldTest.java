package io.github.binaryfoo.decoders.bit;

import io.github.binaryfoo.bit.BitPackage;
import io.github.binaryfoo.bit.EmvBit;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class NumericBitStringFieldTest {

    @Test
    public void getHighNibble() throws Exception {
        NumericBitStringField field = new NumericBitStringField(2, 8, 5, "A number");
        assertThat(field.getValueIn(BitPackage.fromHex("00F0")), is("A number = 15"));
        assertThat(field.getPositionIn(null), is("Byte 2 Bits 8-5"));
        assertThat(field.getStartBytesOffset(), is(1));
        assertThat(field.getLengthInBytes(), is(1));
    }

    @Test
    public void edgeCases() throws Exception {
        NumericBitStringField field = new NumericBitStringField(2, 8, 6, "Y");
        assertThat(field.getValueIn(BitPackage.fromHex("0010")), is("Y = 0"));
        assertThat(field.getValueIn(BitPackage.fromHex("0020")), is("Y = 1"));
        assertThat(field.getValueIn(BitPackage.fromHex("0100")), is("Y = 0"));
    }

    @Test
    public void getLowNibble() throws Exception {
        NumericBitStringField field = new NumericBitStringField(1, 4, 1, "X");
        assertThat(field.getValueIn(BitPackage.fromHex("0C")), is("X = 12"));
        assertThat(field.getPositionIn(null), is("Byte 1 Bits 4-1"));
        assertThat(field.getStartBytesOffset(), is(0));
        assertThat(field.getLengthInBytes(), is(1));
    }
}