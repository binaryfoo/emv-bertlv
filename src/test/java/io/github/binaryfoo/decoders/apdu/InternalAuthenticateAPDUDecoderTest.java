package io.github.binaryfoo.decoders.apdu;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.EmvTags;
import io.github.binaryfoo.decoders.DecodeSession;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static io.github.binaryfoo.DecodedMatcher.decodedAs;
import static io.github.binaryfoo.EmvTags.*;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class InternalAuthenticateAPDUDecoderTest {
    private DecodeSession session;
    private InternalAuthenticateAPDUDecoder internalAuthenticateAPDUDecoder;

    @Before
    public void setUp() throws Exception {
        session = new DecodeSession();
        session.setTagMetaData(METADATA);
        internalAuthenticateAPDUDecoder = new InternalAuthenticateAPDUDecoder();
    }

    @Test
    public void testDecode() throws Exception {
        String input = "008800001D000000100100000000000000003600000000000036141001003F9EAF3300";
        int startIndex = 4;
        DecodedData decodedData = internalAuthenticateAPDUDecoder.decode(input, startIndex, session);
        assertThat(decodedData.getRawData(), is("C-APDU: Internal Authenticate"));
        assertThat(decodedData.getDecodedData(), is("000000100100000000000000003600000000000036141001003F9EAF33"));
        assertThat(decodedData.getStartIndex(), is(startIndex));
        assertThat(decodedData.getEndIndex(), is(startIndex+35));
    }

    @Test
    public void decodeWithDDOL() throws Exception {
        session.put(EmvTags.DDOL, "9F0206 9F0306 9F1A02 9505 5F2A02 9A03 9C01 9F3704".replace(" ", ""));
        String input = "00 88 00 00 1D 000000100100 000000000000 0036 0000000000 0036 141002 00 B77E064F 00".replace(" ", "");
        DecodedData decodedData = internalAuthenticateAPDUDecoder.decode(input, 0, session);
        List<DecodedData> ddolValues = decodedData.getChildren();
        assertThat(ddolValues.get(0), is(decodedAs(AMOUNT_AUTHORIZED.toString(METADATA), "000000100100")));
        assertThat(ddolValues.get(1), is(decodedAs(AMOUNT_OTHER.toString(METADATA), "000000000000")));
        assertThat(ddolValues.get(2), is(decodedAs(TERMINAL_COUNTRY_CODE.toString(METADATA), "0036")));
        assertThat(ddolValues.get(3), is(decodedAs(TERMINAL_VERIFICATION_RESULTS.toString(METADATA), "0000000000")));
        assertThat(ddolValues.get(4), is(decodedAs(TERMINAL_CURRENCY_CODE.toString(METADATA), "AUD (Australian Dollar)")));
        assertThat(ddolValues.get(5), is(decodedAs(TRANSACTION_DATE.toString(METADATA), "141002")));
        assertThat(ddolValues.get(6), is(decodedAs(TRANSACTION_TYPE.toString(METADATA), "00")));
        assertThat(ddolValues.get(7), is(decodedAs(UNPREDICTABLE_NUMBER.toString(METADATA), "B77E064F")));
    }
}
