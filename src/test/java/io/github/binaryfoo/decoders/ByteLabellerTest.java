package io.github.binaryfoo.decoders;

import org.junit.Test;

import static io.github.binaryfoo.decoders.ByteLabeller.labelFor;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

public class ByteLabellerTest {

    @Test
    public void labelsByteLeftToRight() throws Exception {
        assertThat(labelFor("8000"), containsString("Byte 1"));
        assertThat(labelFor("0800"), containsString("Byte 1"));
        assertThat(labelFor("0080"), containsString("Byte 2"));
        assertThat(labelFor("0008"), containsString("Byte 2"));
        assertThat(labelFor("000001"), containsString("Byte 3"));
    }

    @Test
    public void labelsBitsRightToLeft() throws Exception {
        assertThat(labelFor("80"), containsString("Bit 8"));
        assertThat(labelFor("40"), containsString("Bit 7"));
        assertThat(labelFor("20"), containsString("Bit 6"));
        assertThat(labelFor("10"), containsString("Bit 5"));
        assertThat(labelFor("08"), containsString("Bit 4"));
        assertThat(labelFor("04"), containsString("Bit 3"));
        assertThat(labelFor("02"), containsString("Bit 2"));
        assertThat(labelFor("01"), containsString("Bit 1"));
    }
}