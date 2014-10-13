package io.github.binaryfoo.decoders;

import io.github.binaryfoo.DecodedData;
import org.junit.Test;

import java.util.List;

import static io.github.binaryfoo.BoundsMatcher.hasBounds;
import static io.github.binaryfoo.bit.BitPackage.labelFor;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

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

    @Test
    public void decodeBits() throws Exception {
        int startIndexInBytes = 3;
        List<DecodedData> decoded = new ByteLabeller().decode("0080", startIndexInBytes, new DecodeSession());
        assertThat(decoded.size(), is(16));
        assertThat(decoded.get(0).getRawData(), is("Byte 1, Bit 8 = 0"));
        assertThat(decoded.get(0), hasBounds(3, 4));
        assertThat(decoded.get(8).getRawData(), is("Byte 2, Bit 8 = 1"));
        assertThat(decoded.get(8), hasBounds(4, 5));
        assertThat(decoded.get(15).getRawData(), is("Byte 2, Bit 1 = 0"));
        assertThat(decoded.get(15), hasBounds(4, 5));
    }

    @Test
    public void decodeAllOnes() throws Exception {
        List<DecodedData> decoded = new ByteLabeller().decode("ffff", 0, new DecodeSession());
        assertThat(decoded.size(), is(16));
        for (int i = 0; i < 16; i++) {
            assertThat(decoded.get(i).getRawData(), is(String.format("Byte %d, Bit %d = 1", i/8 + 1, 8 - i%8)));
        }
    }

    @Test
    public void decodeAllZeroes() throws Exception {
        List<DecodedData> decoded = new ByteLabeller().decode("0000", 0, new DecodeSession());
        assertThat(decoded.size(), is(16));
        for (int i = 0; i < 16; i++) {
            assertThat(decoded.get(i).getRawData(), is(String.format("Byte %d, Bit %d = 0", i/8 + 1, 8 - i%8)));
        }
    }
}