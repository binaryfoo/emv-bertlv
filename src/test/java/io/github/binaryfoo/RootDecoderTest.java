package io.github.binaryfoo;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class RootDecoderTest {

    @Test
    public void keepTryingWhenDecodeFailsOnOneAPDU() throws Exception {
        String apdus = "00B2020C00\n" +
                        "700E9F080200019F420209789F4A01829000\n" +
                        "00AABBCCDDEE\n" +
                        "00B2013400\n";
        List<DecodedData> decoded = decodeApdus(apdus);
        assertThat(decoded.size(), is(4));
    }

    @Test
    public void decodingResponseToGenerateACUSesCDOLInCommand() throws Exception {
        String apdus = "80AE40001F3030000000101000000000000000003602000080000036141002005563312100\n" +
                       "80124000015C221DC28EB72FCF060201036400009000";
        List<DecodedData> decoded = decodeApdus(apdus);

        DecodedData decodedRApdu = decoded.get(1);
        assertThat(decodedRApdu.getRawData(), is("R-APDU"));
        assertThat(decodedRApdu.getDecodedData(), is("9000"));

        DecodedData decodedResponseTemplate = decodedRApdu.getChildren().get(0);
        assertThat(decodedResponseTemplate.getRawData(), is("80 (Fixed response template)"));

        DecodedData decoded9f27 = decodedResponseTemplate.getChildren().get(0);
        assertThat(decoded9f27.getRawData(), is("9F27 (cryptogram information data)"));
        assertThat(decoded9f27.getDecodedData(), is("TC (Transaction Certificate - Approved)"));

        DecodedData decodedIAD = decodedResponseTemplate.getChildren().get(3);
        assertThat(decodedIAD.getRawData(), is("9F10 (issuer application data)"));

        DecodedData decodedCvr = decodedIAD.getChildren().get(2);
        assertThat(decodedCvr.getRawData(), is("Card verification results"));
        assertThat(decodedCvr.getChildren().get(2).getDecodedData(), is("(Byte 2 Bit 3) Offline PIN performed"));
    }

    @Test
    public void decodePutData() throws Exception {
        String apdus = "04DA9F58090098636D71A294BB85";
        List<DecodedData> decoded = decodeApdus(apdus);
        assertThat(decoded.get(0).getRawData(), is("C-APDU: Put Data"));
        assertThat(decoded.get(0).getEndIndex(), is(14));
    }

    private List<DecodedData> decodeApdus(String apdus) {
        return new RootDecoder().decode(apdus, "EMV", "apdu-sequence");
    }
}