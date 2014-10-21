package io.github.binaryfoo;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

public class RootDecoderTest {

    private RootDecoder rootDecoder;

    @Before
    public void setUp() throws Exception {
        rootDecoder = new RootDecoder();
    }

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
        assertThat(decodedCvr.getChildren().get(2).getRawData(), is("Byte 2 Bit 3 = 1"));
        assertThat(decodedCvr.getChildren().get(2).getDecodedData(), is("Offline PIN Verification Performed"));
    }

    @Test
    public void decodePutData() throws Exception {
        String apdus = "04DA9F58090098636D71A294BB85";
        List<DecodedData> decoded = decodeApdus(apdus);
        assertThat(decoded.get(0).getRawData(), is("C-APDU: Put Data"));
        assertThat(decoded.get(0).getEndIndex(), is(14));
    }

    @Test
    public void decodeAIP() throws Exception {
        List<DecodedData> decoded = rootDecoder.decode("4000", "EMV", EmvTags.APPLICATION_INTERCHANGE_PROFILE.toString());
        assertThat(decoded.get(0).getRawData(), is("4000 (Byte 1 Bit 7)"));
        assertThat(decoded.get(0).getDecodedData(), is("SDA supported"));
    }


    @Test
    public void decodeStaticSignedData() throws Exception {
        String apdus = "00 A4 04 00 07 A0 00 00 00 65 10 10 00\n" +
                "6F 32 84 07 A0 00 00 00 65 10 10 A5 27 50 0A 4A 43 42 20 43 72 65 64 69 74 87 01 01 5F 2D 04 6A 61 65 6E 9F 11 01 01 9F 12 0A 4A 43 42 20 43 72 65 64 69 74 90 00\n" +
                "00 B2 05 0C 00\n" +
                "70 81 A0 8F 01 0F 90 81 90 3F E0 71 C9 3A 89 F7 79 AD D7 40 B7 12 50 2E 69 D6 AE 9E 22 6D CF 75 88 D2 2D 95 61 AB 44 EC C1 C9 1D 42 89 E1 9A 48 51 6A F9 26 55 37 63 ED E1 F8 F3 0A 4E D7 CE 3D B4 73 B4 50 AE 31 73 6B 52 D7 E7 10 D9 29 32 54 A4 1B 79 C6 5B 62 CF AC 52 77 10 53 8F E4 AA F3 36 A2 78 0C A4 04 59 61 E3 45 DA 2B 77 5B AF 18 24 FF E7 56 52 4B 79 56 F3 00 A3 2D 16 23 FF F7 6F 53 A3 06 02 4B 28 9A 17 7D 59 A0 8D 14 98 67 EB 5C 23 1D FA DE C4 C7 9B 92 04 CB 2E 2B 9F 9F 32 01 03 90 00\n" +
                "00 B2 06 0C 00\n" +
                "70 76 9F 4A 01 82 93 70 27 6F 99 43 5D AC C0 20 C1 AB 9D 09 DA D7 6D 70 44 C6 D5 74 C9 F0 C0 5D A1 D6 68 94 EF A2 BF 9A 68 6E 1D FA 35 38 56 D6 CF 24 C7 68 7B 99 91 D6 3C 87 7F 09 26 42 40 41 09 63 56 E0 0E 24 BB F4 02 62 6A 37 D9 8E A4 25 08 FC 58 28 52 7A BE 1D 3F 2C 28 D6 0B C2 49 CF 20 38 C7 70 22 DF D5 92 3F 02 99 D6 05 48 23 86 ED 94 7E 8E CB B4 35 B6 90 00";

        List<DecodedData> decoded = decodeApdus(apdus.replace(" ", ""));
        assertThat(DecodedData.findForTag(EmvTags.ISSUER_PUBLIC_KEY_CERTIFICATE, decoded).getNotes(), is(
                "Header: 6A\n" +
                "Format: 02\n" +
                "Identifier (PAN prefix): 35699900\n" +
                "Expiry Date (MMYY): 1249\n" +
                "Serial number: 0006B0\n" +
                "Hash algorithm: 01\n" +
                "Public key algorithm: 01\n" +
                "Public key length: 70 (112)\n" +
                "Public key exponent length: 01\n" +
                "Public key: BD46CCE01464D4C4269D4CC5FB616119C4354ED18C85A936F9C44A7B0EED96AD97DB59D84B05A6E250631BA79FA7D75B07DD4586CE7700C64FFBB60EDB15E29C715D771C948999187EAEDF60CE18FE67E99C8BCE9DE31655E7EB9B692F502573CA65B7A7C167F86D9AAB90A9\n" +
                "          : BD46CCE01464D4C4269D4CC5FB616119C4354ED18C85A936F9C44A7B0EED96AD97DB59D84B05A6E250631BA79FA7D75B07DD4586CE7700C64FFBB60EDB15E29C715D771C948999187EAEDF60CE18FE67E99C8BCE9DE31655E7EB9B692F502573CA65B7A7C167F86D9AAB90A9F9D10CF1\n" +
                "Hash: F9D10CF1387465F4FCAD14EEFDAFEE10A50B7C08\n" +
                "Trailer: BC\n"));
        // TODO assert signed static data has notes
    }

    private List<DecodedData> decodeApdus(String apdus) {
        return rootDecoder.decode(apdus, "EMV", "apdu-sequence");
    }
}