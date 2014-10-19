package io.github.binaryfoo.decoders;

import io.github.binaryfoo.DecodedData;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.List;

import static io.github.binaryfoo.BoundsMatcher.hasBounds;
import static io.github.binaryfoo.DecodedMatcher.decodedAs;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class EmvBitStringDecoderTest {

    @Test
    public void decodeSomeBits() throws Exception {
        EmvBitStringDecoder decoder = decoderFor("(1,8)=1 & (1,7)=1 : One\n" +
                                                 "(1,8)=1 & (1,7)=0 : Two\n" +
                                                 "(2,1)=1 : Three");

        int startIndex = 4;
        List<DecodedData> decoded = decoder.decode("C000", startIndex, new DecodeSession());
        assertThat(decoded.get(0).getRawData(), is("Byte 1 Bit 8 = 1, Byte 1 Bit 7 = 1"));
        assertThat(decoded.get(0).getDecodedData(), is("One"));
        assertThat(decoded.get(0), hasBounds(startIndex, startIndex + 1));
    }

    @Test
    public void canIncludeHexStringInFieldDescription() throws Exception {
        EmvBitStringDecoder decoder = decoderFor("(1,8)=1 & (1,7)=0 : Two", true);

        List<DecodedData> decoded = decoder.decode("8000", 0, new DecodeSession());
        assertThat(decoded.get(0).getRawData(), is("8000 (Byte 1 Bit 8 = 1, Byte 1 Bit 7 = 0)"));
    }

    @Test
    public void decodeNumericField() throws Exception {
        EmvBitStringDecoder decoder = decoderFor("(1,8-5)=INT : A number\n");

        int startIndex = 4;
        List<DecodedData> decoded = decoder.decode("C000", startIndex, new DecodeSession());
        assertThat(decoded.get(0).getRawData(), is("Byte 1 Bits 8-5"));
        assertThat(decoded.get(0).getDecodedData(), is("A number = 12"));
        assertThat(decoded.get(0), hasBounds(startIndex, startIndex + 1));
    }

    @Test
    public void decodeFullByteMatchField() throws Exception {
        EmvBitStringDecoder decoder = decoderFor("(1)=0x01 : Bit 1\n(1)=0x3F : Three F\n(2)=0x81 : Four\n");

        assertThat(decoder.decode("01", 0, new DecodeSession()), hasItem(decodedAs("Byte 1 = 0x01", "Bit 1")));
        assertThat(decoder.decode("3F", 0, new DecodeSession()), hasItem(decodedAs("Byte 1 = 0x3F", "Three F")));
        assertThat(decoder.decode("00", 0, new DecodeSession()), is(Collections.<DecodedData>emptyList()));
        assertThat(decoder.decode("0081", 0, new DecodeSession()), hasItem(decodedAs("Byte 2 = 0x81", "Four")));
    }

    @Test
    public void ignoreCommentsAndBlankLines() throws Exception {
        decoderFor("# comment\n\n\n");
    }

    @Test
    public void maxLength() throws Exception {
        EmvBitStringDecoder oneByteDecoder = decoderFor("(1,1)=1:A");
        assertThat(oneByteDecoder.getMaxLength(), is(2));

        EmvBitStringDecoder twoByteDecoder = decoderFor("(1,8)=0:A\n(2,8)=1:A\n");
        assertThat(twoByteDecoder.getMaxLength(), is(4));
    }

    @Test
    public void validate() {
        EmvBitStringDecoder decoder = decoderFor("(1,8)=0:A\n(2,8)=1:A\n");
        String lengthErrorMessage = "Value must be exactly 4 characters";
        assertEquals(lengthErrorMessage, decoder.validate("000"));
        assertEquals(lengthErrorMessage, decoder.validate("00000"));
        assertEquals(lengthErrorMessage, decoder.validate(null));
        assertEquals(lengthErrorMessage, decoder.validate(""));

        String charactersErrorMessage = "Value must contain only the characters 0-9 and A-F";
        assertEquals(charactersErrorMessage, decoder.validate("xxxx"));

        assertEquals(null, decoder.validate("0000"));
        assertEquals(null, decoder.validate("1111"));
        assertEquals(null, decoder.validate("ffff"));
    }

    private EmvBitStringDecoder decoderFor(String s) {
        return decoderFor(s, false);
    }

    private EmvBitStringDecoder decoderFor(String s, boolean showFieldHexInDecode) {
        return new EmvBitStringDecoder((new ByteArrayInputStream(s.getBytes())), showFieldHexInDecode);
    }
}