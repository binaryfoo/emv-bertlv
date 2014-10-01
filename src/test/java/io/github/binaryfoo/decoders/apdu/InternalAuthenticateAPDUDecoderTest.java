package io.github.binaryfoo.decoders.apdu;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.decoders.DecodeSession;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class InternalAuthenticateAPDUDecoderTest {
    @Test
    public void testDecode() throws Exception {
        String input = "008800001D000000100100000000000000003600000000000036141001003F9EAF3300";
        int startIndex = 4;
        DecodeSession session = new DecodeSession();
        DecodedData decodedData = new InternalAuthenticateAPDUDecoder().decode(input, startIndex, session);
        assertThat(decodedData.getRawData(), is("C-APDU: Internal Authenticate"));
        assertThat(decodedData.getDecodedData(), is("000000100100000000000000003600000000000036141001003F9EAF33"));
        assertThat(decodedData.getStartIndex(), is(startIndex));
        assertThat(decodedData.getEndIndex(), is(startIndex+35));
    }
}
