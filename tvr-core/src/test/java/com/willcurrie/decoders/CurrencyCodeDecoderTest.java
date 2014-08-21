package com.willcurrie.decoders;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CurrencyCodeDecoderTest {

    private CurrencyCodeDecoder decoder = new CurrencyCodeDecoder();

    @Test
    public void decodeAUD() throws Exception {
        String decoded = decoder.decode("0036");
        assertThat(decoded, is("AUD (Australian Dollar)"));
    }
}