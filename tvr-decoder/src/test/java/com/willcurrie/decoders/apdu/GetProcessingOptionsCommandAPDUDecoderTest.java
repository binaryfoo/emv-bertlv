package com.willcurrie.decoders.apdu;

import com.willcurrie.DecodedData;
import com.willcurrie.EmvTags;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.collection.IsCollectionContaining.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class GetProcessingOptionsCommandAPDUDecoderTest {

    @Test
    public void testDecode() throws Exception {
        DecodeSession session = new DecodeSession();
        session.put(EmvTags.PDOL, "9F66049F02069F03069F1A0295055F2A029A039C019F3704");
        String input = "80A8000023832136000000000000001000000000000000003600000000000036120315000008E4C800";
        DecodedData decoded = new GetProcessingOptionsCommandAPDUDecoder().decode(input, 0, session);
        assertThat(decoded.getRawData(), is("C-APDU: GPO"));
        List<DecodedData> children = decoded.getChildren();
        assertThat(children, hasItem(new DecodedData(EmvTags.TERMINAL_TX_QUALIFIERS, "36000000", 7, 11)));
        assertThat(children, hasItem(new DecodedData(EmvTags.UNPREDICTABLE_NUMBER, "0008E4C8", 36, 40)));
    }
}
