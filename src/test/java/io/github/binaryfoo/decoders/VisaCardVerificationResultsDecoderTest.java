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
                DecodedData.primitive("Byte 2 Bit 8 = 1, Byte 2 Bit 7 = 0", "Second GENERATE AC not requested", startIndex + 1, startIndex + 2),
                DecodedData.primitive("Byte 2 Bit 6 = 1, Byte 2 Bit 5 = 0", "ARQC Returned in GPO/first GENERATE AC", startIndex + 1, startIndex + 2),
                DecodedData.primitive("Byte 2 Bit 3 = 1", "Offline PIN Verification Performed", startIndex + 1, startIndex + 2),
                DecodedData.primitive("Byte 3 Bit 8 = 1", "Last online transaction not completed", startIndex + 2, startIndex + 3),
                DecodedData.primitive("Byte 3 Bit 6 = 1", "Exceeded velocity checking counters", startIndex + 2, startIndex + 3),
                DecodedData.primitive("Byte 3 Bit 5 = 1", "New card", startIndex + 2, startIndex + 3),
                DecodedData.primitive("Byte 3 Bit 4 = 1", "Issuer Authentication failure on last online transaction", startIndex + 2, startIndex + 3),
                DecodedData.primitive("Byte 4 Bits 8-5", "Issuer Script Commands processed on last transaction = 0", startIndex + 3, startIndex + 4)
        )));
    }

    @Test
    public void issuerScriptCount() throws Exception {
        assertThat(decoder.decode("00000010", 0, new DecodeSession()).get(2).getDecodedData(), is("Issuer Script Commands processed on last transaction = 1"));
        assertThat(decoder.decode("00000020", 0, new DecodeSession()).get(2).getDecodedData(), is("Issuer Script Commands processed on last transaction = 2"));
        assertThat(decoder.decode("000000F0", 0, new DecodeSession()).get(2).getDecodedData(), is("Issuer Script Commands processed on last transaction = 15"));
    }
}
