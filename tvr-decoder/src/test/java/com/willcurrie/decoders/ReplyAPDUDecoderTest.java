package com.willcurrie.decoders;

import com.willcurrie.DecodedData;
import com.willcurrie.EmvTags;
import com.willcurrie.decoders.apdu.ReplyAPDUDecoder;
import org.junit.Test;

import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ReplyAPDUDecoderTest {
    @Test
    public void testDecode() throws Exception {
        String input = "6F1C8407A0000000041010A511500F505043204D434420303420207632309000";
        DecodedData decoded = new ReplyAPDUDecoder(new TLVDecoder()).decode(input, 12, new DecodeSession());
        assertThat(decoded.getRawData(), is("R-APDU"));
        assertThat(decoded.getDecodedData(), is("9000"));
        assertThat(decoded.getStartIndex(), is(12));
        assertThat(decoded.getEndIndex(), is(12 + input.length()/2));
    }

    @Test
    public void testReplyWithPDOL() throws Exception {
        String input = "6F318407A0000000031010A52650095649534120544553549F38189F66049F02069F03069F1A0295055F2A029A039C019F37049000";
        DecodeSession session = new DecodeSession();
        DecodedData decoded = new ReplyAPDUDecoder(new TLVDecoder()).decode(input, 0, session);
        assertThat(decoded.getRawData(), is("R-APDU"));
        assertThat(session, hasEntry(EmvTags.PDOL, "9F66049F02069F03069F1A0295055F2A029A039C019F3704"));
    }
}
