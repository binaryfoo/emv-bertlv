package io.github.binaryfoo;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static io.github.binaryfoo.BoundsMatcher.hasBounds;
import static io.github.binaryfoo.DecodedAsStringMatcher.decodedAsString;
import static io.github.binaryfoo.DecodedMatcher.decodedAs;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

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
        assertThat(decoded9f27.getBackgroundReading(), hasEntry("short", "Message the cryptogram should convey to the issuer: approved, declined, can I approve?"));

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

        String recoveredIssuerCertificate = "Recovered using CA public key (A000000065,0F)\n" +
                "Header: 6A\n" +
                "Format: 02\n" +
                "Identifier (PAN prefix): 35699900\n" +
                "Expiry Date (MMYY): 1249\n" +
                "Serial number: 0006B0\n" +
                "Hash algorithm: 01\n" +
                "Public key algorithm: 01\n" +
                "Public key length: 112\n" +
                "Public key exponent length: 01\n" +
                "Public key: BD46CCE01464D4C4269D4CC5FB616119C4354ED18C85A936F9C44A7B0EED96AD97DB59D84B05A6E250631BA79FA7D75B07DD4586CE7700C64FFBB60EDB15E29C715D771C948999187EAEDF60CE18FE67E99C8BCE9DE31655E7EB9B692F502573CA65B7A7C167F86D9AAB90A9\n" +
                "Hash: F9D10CF1387465F4FCAD14EEFDAFEE10A50B7C08\n" +
                "Trailer: BC\n";

        String recoveredStaticSignedData = "Recovered using Issuer public key\n" +
                "Header: 6A\n" +
                "Format: 03\n" +
                "Hash Algorithm: 01\n" +
                "Data Auth Code: 0001\n" +
                "Hash: F6E1F5D3652F06C2D5F9E6599AE8ED5BE1D575CF\n" +
                "Trailer: BC\n";

        List<DecodedData> decoded = decodeApdus(apdus.replace(" ", ""));
        DecodedData issuerCertificate = DecodedData.findForTag(EmvTags.ISSUER_PUBLIC_KEY_CERTIFICATE, decoded);
        assertThat(issuerCertificate.getChildren(), is(decodedAsString(recoveredIssuerCertificate)));
        assertThat(issuerCertificate.getChildren().get(1), is(decodedAs("Header", "6A")));
        assertThat(issuerCertificate.getChildren().get(1), hasBounds(issuerCertificate.getStartIndex() + 3, issuerCertificate.getStartIndex() + 4));
        assertThat(issuerCertificate.getBackgroundReading(), hasEntry("short", "Public key owned by the card issuer signed by the Scheme (CA)"));
        assertThat(issuerCertificate.getBackgroundReading(), hasEntry("long", "In turn used to sign the static or dynamic data provided by the card."));

        DecodedData decodedSSAD = DecodedData.findForTag(EmvTags.SIGNED_STATIC_APPLICATION_DATA, decoded);
        assertThat(decodedSSAD.getChildren(), is(decodedAsString(recoveredStaticSignedData)));
        assertThat(decodedSSAD.getChildren().get(1), is(decodedAs("Header", "6A")));
        assertThat(decodedSSAD.getChildren().get(1), hasBounds(decodedSSAD.getStartIndex() + 2, decodedSSAD.getStartIndex() + 3));
    }

    @Test
    public void decodeDynamicSignedData_InternalAuthenticate() throws Exception {
        String apdus = "00 A4 04 00 08 A0 00 00 00 25 01 01 04 00\n" + // Command: select
                "6F 2A 84 08 A0 00 00 00 25 01 01 04 A5 1E 50 04 41 4D 45 58 87 01 01 9F 38 12 9F 1A 02 9F 33 03 9F 40 05 9F 1B 04 9F 09 02 9F 35 01 90 00 \n" + // Response: has AID
                "00 B2 01 0C 00\n" + // Command: read record
                "70 81 A1 5F 20 1A 41 4D 45 58 20 54 45 53 54 43 41 52 44 20 20 20 20 20 20 20 20 20 20 20 20 20 57 13 37 42 45 45 54 00 00 1D 20 12 20 10 00 40 56 56 00 00 0F 8F 01 04 90 60 94 D2 56 91 DC 52 0C E1 82 B5 F0 28 EE D8 4E A0 78 35 23 A9 0F 77 2F C5 0A E6 4C 31 3A 2F B8 1D 09 95 4B BA F0 6F FF 1C 73 64 FB 17 14 F5 04 9D 78 CA F4 D7 7C 36 36 2B 2C 9A 59 73 03 A1 E2 9C A7 75 D0 74 1E 79 EE D5 8F 45 F2 CB 73 24 7A EF 14 1A 65 B8 46 48 39 FC B0 55 E3 2E 0C 2A 93 F4 9F 32 01 03 92 04 D2 48 71 61 90 00\n" + // Response: Issuer public key
                "00 B2 04 0C 00\n" + // Command: read record
                "70 81 8C 9F 46 40 35 2D 92 83 E0 E1 23 73 F1 8C BA CD 76 0A 3E 78 D0 04 99 0E A8 5E B2 4A DC B3 A0 93 26 AD AA 4A E7 CE DF 26 29 01 94 93 AE C4 67 98 9F 43 16 28 70 C9 48 BF 5B 43 D1 96 81 47 8C F0 5A B2 6D B5 9F 48 2A FE 6F 90 DD D4 32 B5 DB 1C DC FB AC 96 98 02 C6 4A 60 69 FD 26 7C 41 C1 AC 82 E3 8E A2 54 F4 AA 4B D0 9B 04 51 6C 19 E1 8A C5 9F 47 01 03 9F 49 15 9F 02 06 9F 03 06 9F 1A 02 95 05 5F 2A 02 9A 03 9C 01 9F 37 04 90 00\n" + // Response: ICC public key
                "00 88 00 00 1D 00 00 00 10 01 00 00 00 00 00 00 00 00 36 00 00 00 00 00 00 36 14 10 02 00 B7 7E 06 4F 00\n" + // Command: Internal Authenticate
                "80 40 2E 52 9F B6 4E 6E 54 1D 01 2B 6B 05 B0 F3 25 4F 51 26 7A C9 0A 1F 27 45 77 38 74 28 8C 19 99 35 C3 B1 77 43 AC 8A CB E1 90 5B DA 92 FF D0 39 30 37 69 90 BF D7 BF 18 77 10 50 C5 D9 E7 04 81 38 90 00"; // Response: Signed Data

        String recoveredDynamicSignedData = "Recovered using ICC public key\n" +
                "Header: 6A\n" +
                "Format: 05\n" +
                "Hash algorithm: 01\n" +
                "Dynamic data length: 7\n" +
                "ICC dynamic number length: 6\n" +
                "ICC dynamic number: 112233445566\n" + // not so dynamic looking...
                "Hash: 97C21EB1AA67291E00322913CE1C52CCF0D93200\n" +
                "Trailer: BC\n";

        List<DecodedData> decoded = decodeApdus(apdus.replace(" ", ""));
        DecodedData internalAuthResponseTemplate = decoded.get(decoded.size() - 1);
        DecodedData decodedSDAD = internalAuthResponseTemplate.getChild(0);
        assertThat(decodedSDAD.getChildren(), is(decodedAsString(recoveredDynamicSignedData)));
        assertThat(decodedSDAD.getChildren().get(1), is(decodedAs("Header", "6A")));
        assertThat(decodedSDAD.getChildren().get(1), hasBounds(internalAuthResponseTemplate.getStartIndex() + 2, internalAuthResponseTemplate.getStartIndex() + 3));
    }

    @Test
    public void decodedCombinedDataAuth_MagstripeMode() throws Exception {
        String apdus = "00A404000E325041592E5359532E444446303100\n" +
                "6F36840E325041592E5359532E4444463031A524BF0C21611F4F08A0000000250109015010414D45524943414E20455850524553538701019000\n" +
                "00A4040008A00000002501090100\n" +
                "6F248408A000000025010901A5185010416D65726963616E20457870726573739F38039F35019000\n" +
                "80A80000038301E200\n" +
                "771882021940940C0801020028010101280204009F71030100029000\n" +
                "00B2010C00\n" +
                "70375F201A5850204D4F42494C4520303320202020202020202020202020205713374245455400001D161172612110000000000F5F300207219000\n" +
                "00B2020C00\n" +
                "700E9F080200019F420209789F4A01829000\n" +
                "00B2012C00\n" +
                "70519F0702FF005F280206205F25031211015F24031611305A08374245455400001F5F3401009F0D05F850ECA0009F0E0500000000009F0F05F078FCF8008E0E0000000000000000420141031F029F700218009000\n" +
                "00B2022C00\n" +
                "7081939081903F19744F14EF8BEFD408E03A80938535587DAE7608C64FB5831421545184EAF806844BBD35E26233B326334574E9662B3A9397108B760190F5DEFB1D92BA032D8AAA9ACF778F35BDDFD3DCF53F41BE614AE8542E5BCDF14E9A542F2D4F2009F7D06AE5865D6B315CB7358EBA89CA55DC157C61DE34BDD71A9DD80C5E16DCBFC9A3DFCFDFECA2B7E1186964CFF2C0DE639000\n" +
                "00B2032C00\n" +
                "7081849F46818043579580B295A5E163967326ADDE10FAC9BFD30978AE2BCFF2F74FA74753AF4D2C9C287B503176EAE3A07E36A3A5D03260FDC2BF8526AF30AAF08A16D91DFF2A6451C798DD6F3BBFA05CAFD6B5E2AF1BDCD0979723E44247B72E6C16D2FF84AE9B5450F773893C6B566B6CD0F0A987BC9DE6B4E962CF5635DB65CDA539D5C65F9000\n" +
                "00B2042C00\n" +
                "704A8C039F37048D058A029F37048F01979214706C987F58ADD706BE011D8EA7C2C755A956C2CB9F3201039F4701039F481AC107F69F1A498EC910B29E566DC817DD7AA28EE7C21FDD7D8D239000\n" +
                "80CA9F3600\n" +
                "9F360200239000\n" +
                "80AE5000040000101100\n" +
                "7781869F2701809F360200239F4B7035CAD84ECAA6AD8F85978AC61B591955347D57CCE520F8A2FA84944EE36FEEE448431342D1FB4567D02E0251F96B335686833B222908BD8DC2660FAD29C85AFD3D3A6A18E9D22276FC58378C8277E084EB3AB35CE7108A5B7093FE1A57B4E530518F5218C1148BE6C609BD700AEB1B6E9F100706020203A400009000\n";

        String recoveredStaticSignedData = "Recovered using ICC public key\n" +
                "Header: 6A\n" +
                "Format: 05\n" +
                "Hash algorithm: 01\n" +
                "Dynamic data length: 38\n" +
                "ICC dynamic number length: 8\n" +
                "ICC dynamic number: 5F4BEB331DEFFB8E\n" +
                "Cryptogram information data: 80\n" +
                "Cryptogram: 34008DDE8A57918A\n" +
                "Transaction data hash code: F5637605172DDFA7B55E8BD949B42C44465A8F81\n" +
                "Hash: 1F08D1D6A8E4582EE63A454E537148A6BDB9FC9C\n" +
                "Trailer: BC\n";

        List<DecodedData> decoded = decodeApdus(apdus);

        DecodedData decodedSSAD = DecodedData.findForTag(EmvTags.SIGNED_DYNAMIC_APPLICATION_DATA, decoded);
        assertThat(decodedSSAD.getChildren(), is(decodedAsString(recoveredStaticSignedData)));
        assertThat(decodedSSAD.getChildren().get(1), is(decodedAs("Header", "6A")));
        assertThat(decodedSSAD.getChildren().get(1), hasBounds(decodedSSAD.getStartIndex() + 3, decodedSSAD.getStartIndex() + 4));
    }

    @Test
    public void addsBackgroundReadingToReadRecordCommand() throws Exception {
        String apdus = "00B2011400";

        List<DecodedData> decoded = decodeApdus(apdus);
        assertThat(decoded.get(0).getBackgroundReading(), is(notNullValue()));
    }

    private List<DecodedData> decodeApdus(String apdus) {
        return rootDecoder.decode(apdus, "EMV", "apdu-sequence");
    }
}