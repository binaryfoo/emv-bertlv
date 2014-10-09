package io.github.binaryfoo.decoders;

import io.github.binaryfoo.DecodedData;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import static io.github.binaryfoo.BoundsMatcher.hasBounds;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class EmvBitStringDecoderTest {

    @Test
    public void decodeSomeBits() throws Exception {
        EmvBitStringDecoder decoder = decoderFor("(1,8)=1 & (1,7)=1 : One\n" +
                                                 "(1,8)=1 & (1,7)=0 : Two\n" +
                                                 "(2,1)=1 : Three");

        int startIndex = 4;
        List<DecodedData> decoded = decoder.decode("C000", startIndex, null);
        assertThat(decoded.get(0).getRawData(), is("Byte 1 Bit 8 = 1, Byte 1 Bit 7 = 1"));
        assertThat(decoded.get(0).getDecodedData(), is("One"));
        assertThat(decoded.get(0), hasBounds(startIndex, startIndex + 1));
    }

    @Test
    public void decodeNumericField() throws Exception {
        EmvBitStringDecoder decoder = decoderFor("(1,8-5) : A number\n");

        int startIndex = 4;
        List<DecodedData> decoded = decoder.decode("C000", startIndex, null);
        assertThat(decoded.get(0).getRawData(), is("Byte 1 Bits 8-5"));
        assertThat(decoded.get(0).getDecodedData(), is("A number = 12"));
        assertThat(decoded.get(0), hasBounds(startIndex, startIndex + 1));
    }

    @Test
    public void ignoreCommentsAndBlankLines() throws Exception {
        decoderFor("# comment\n\n\n");
    }

    private EmvBitStringDecoder decoderFor(String s) {
        return new EmvBitStringDecoder((new ByteArrayInputStream(s.getBytes())));
    }
}