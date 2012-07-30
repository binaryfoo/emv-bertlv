package com.willcurrie.decoders.apdu;

import com.willcurrie.DecodedData;
import com.willcurrie.decoders.DecodeSession;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ExternalAuthenticateAPDUDecoderTest {
    @Test
    public void testDecode() throws Exception {
        String input = "008200000A11223344556677889900";
        int startIndex = 4;
        DecodeSession session = new DecodeSession();
        DecodedData decodedData = new ExternalAuthenticateAPDUDecoder().decode(input, startIndex, session);
        assertThat(decodedData.getRawData(), is("C-APDU: External Authenticate"));
        assertThat(decodedData.getDecodedData(), is("11223344556677889900"));
        assertThat(decodedData.getStartIndex(), is(startIndex));
        assertThat(decodedData.getEndIndex(), is(startIndex+15));
    }
}
