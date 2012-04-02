package com.willcurrie.decoders;

import com.willcurrie.DecodedData;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class IssuerApplicationDataDecoderTest {
    private IssuerApplicationDataDecoder decoder;

    @Before
    public void setUp() throws Exception {
        decoder = new IssuerApplicationDataDecoder();
    }

    @Test
    public void testDecodeVisaIAD() throws Exception {
        String input = "06010A03A4B8000F112233445566778899AABBCCDDEEFF";
        int startIndex= 5;
        DecodeSession session = new DecodeSession();
        List<DecodedData> decodedData = decoder.decode(input, startIndex, session);
        assertThat(decodedData.get(0), is(new DecodedData("Derivation key index", "01", startIndex + 1, startIndex + 2)));
        assertThat(decodedData.get(1), is(new DecodedData("Cryptogram version number", "0A", startIndex + 2, startIndex + 3)));
        assertThat(decodedData.get(2), is(new DecodedData("Card verification results", "03A4B800", startIndex + 3, startIndex + 7)));
        assertThat(decodedData.get(3), is(new DecodedData("Issuer discretionary data", "112233445566778899AABBCCDDEEFF", startIndex + 8, startIndex + 16)));
        assertThat(decodedData.size(), is(4));
    }

    @Test
    public void testDecodeVisaIADWithoutIssuerDiscretionaryData() throws Exception {
        String input = "06010A03A00000";
        int startIndex= 5;
        DecodeSession session = new DecodeSession();
        List<DecodedData> decodedData = decoder.decode(input, startIndex, session);
        assertThat(decodedData.get(0), is(new DecodedData("Derivation key index", "01", startIndex + 1, startIndex + 2)));
        assertThat(decodedData.get(1), is(new DecodedData("Cryptogram version number", "0A", startIndex + 2, startIndex + 3)));
        assertThat(decodedData.get(2), is(new DecodedData("Card verification results", "03A00000", startIndex + 3, startIndex + 7)));
        assertThat(decodedData.size(), is(3));
    }
}
