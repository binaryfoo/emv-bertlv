package io.github.binaryfoo.decoders;

import io.github.binaryfoo.DecodedData;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class VisaCardVerificationResultsDecoderTest {

    private VisaCardVerificationResultsDecoder decoder = new VisaCardVerificationResultsDecoder();

    @Test
    public void testDecode() throws Exception {
        int startIndex = 3;
        List<DecodedData> decoded = decoder.decode("03A4B800", startIndex, new DecodeSession());
        assertThat(decoded, is(Arrays.asList(
            new DecodedData("", "Second GENERATE AC not requested", startIndex + 1, startIndex + 2),
            new DecodedData("", "ARQC returned in GPO/first GENERATE AC", startIndex + 1, startIndex + 2),
            new DecodedData("", "(Byte 2 Bit 3) Offline PIN performed", startIndex + 1, startIndex + 2),
            new DecodedData("", "(Byte 3 Bit 8) Last online transaction not completed", startIndex + 2, startIndex + 3),
            new DecodedData("", "(Byte 3 Bit 6) Exceeded velocity checking counters", startIndex + 2, startIndex + 3),
            new DecodedData("", "(Byte 3 Bit 5) New card", startIndex + 2, startIndex + 3),
            new DecodedData("", "(Byte 3 Bit 4) Issuer Authentication failure on last online transaction", startIndex + 2, startIndex + 3)
        )));
    }
}
