package com.willcurrie.decoders.apdu;

import com.willcurrie.DecodedData;
import com.willcurrie.decoders.DecodeSession;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ExternalAuthenticateAPDUDecoderTest {
    @Test
    public void testDecode() throws Exception {
        String input = "00820000081122334455667788";
        int startIndex = 4;
        DecodeSession session = new DecodeSession();
        DecodedData decodedData = new ExternalAuthenticateAPDUDecoder().decode(input, startIndex, session);
        assertThat(decodedData.getRawData(), is("C-APDU: External Authenticate"));
        assertThat(decodedData.getDecodedData(), is("1122334455667788"));
        assertThat(decodedData.getStartIndex(), is(startIndex));
        assertThat(decodedData.getEndIndex(), is(startIndex+13));

    }
}
