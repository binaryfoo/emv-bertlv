package com.willcurrie.decoders;

import com.willcurrie.DecodedData;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.*;

public class CurrencyCodeDecoderTest {

    private CurrencyCodeDecoder decoder = new CurrencyCodeDecoder();

    @Test
    public void decodeAUD() throws Exception {
        List<DecodedData> decoded = decoder.decode("0036", 1, null);
        assertThat(decoded, hasItem(new DecodedData("0036", "AUD (Australian Dollar)", 1, 3)));
    }
}