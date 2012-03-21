package com.willcurrie.decoders.apdu;

import com.willcurrie.DecodedData;
import com.willcurrie.decoders.DecodeSession;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ComputeCryptoChecksumDecoderTest {

    @Test
    public void testDecode() throws Exception {
        String input = "802A8E80040000000000";
        int startIndex = 5;
        DecodedData decodedData = new ComputeCryptoChecksumDecoder().decode(input, startIndex, new DecodeSession());
        assertThat(decodedData.isComposite(), is(false));
        assertThat(decodedData.getRawData(), is("C-APDU: Compute checksum"));
        assertThat(decodedData.getDecodedData(), is("00000000"));
        assertThat(decodedData.getStartIndex(), is(startIndex));
        assertThat(decodedData.getEndIndex(), is(startIndex + 10));
    }
}
