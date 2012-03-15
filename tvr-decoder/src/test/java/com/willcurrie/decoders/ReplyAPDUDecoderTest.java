package com.willcurrie.decoders;

import com.willcurrie.DecodedData;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ReplyAPDUDecoderTest {
    @Test
    public void testDecode() throws Exception {
        String input = "6F1C8407A0000000041010A511500F505043204D434420303420207632309000";
        DecodedData decoded = new ReplyAPDUDecoder(new TLVDecoder()).decode(input, 12);
        assertThat(decoded.getDecodedData(), is("Reply"));
        assertThat(decoded.getStartIndex(), is(12));
        assertThat(decoded.getEndIndex(), is(12 + input.length()/2));
    }
}
