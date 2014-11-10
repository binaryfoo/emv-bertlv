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

    @Test
    public void handleOddTag() throws Exception {
        String expected = "5F20 (card holder name):  /\n" +
                "5F24 (card expiry): 150831\n" +
                "5F2D (language preference): ruen\n" +
                "5F28 (issuer country code): RUS (Russian Federation)\n" +
                "8F (ca public key index): 05\n" +
                "9F32 (issuer public key exponent): 03\n" +
                "4F (ADF - Application dedicated file name): A0000000041010\n" +
                "50 (application label): MASTERCARD\n" +
                "87 (application priority indicator): 01\n" +
                "82 (AIP - Application Interchange Profile): 0080\n" +
                "  0080 (Byte 2 Bit 8): EMV and Magstripe Modes Supported\n" +
                "9F08 (card application version number): 0002\n" +
                "5F30 (service code): 0221\n" +
                "5F25 (application effective date): 141101\n" +
                "5A (PAN): 5420485537788154\n" +
                "5F34 (PAN sequence number): 01\n" +
                "57 (track 2 equivalent data): 5420485537788154D15082211839409890000F\n" +
                "9F1F (track 1 discretionary data): 183940000000000989000000\n" +
                "9F4A (SDA tag list): 82\n" +
                "8C (CDOL 1 - Card risk management data object list 1): 9F02069F03069F1A0295055F2A029A039C019F37049F35019F45029F...1502\n" +
                "  9F02 (amount authorized) 6 bytes\n" +
                "  9F03 (amount other) 6 bytes\n" +
                "  9F1A (terminal country code) 2 bytes\n" +
                "  95 (TVR - Terminal Verification Results) 5 bytes\n" +
                "  5F2A (terminal currency code) 2 bytes\n" +
                "  9A (transaction date) 3 bytes\n" +
                "  9C (transaction type) 1 bytes\n" +
                "  9F37 (unpredictable number) 4 bytes\n" +
                "  9F35 (terminal type) 1 bytes\n" +
                "  9F45 (data authentication code) 2 bytes\n" +
                "  9F4C (ICC dynamic number) 8 bytes\n" +
                "  9F34 (CVM Results - Cardholder Verification Results) 3 bytes\n" +
                "  9F15 (merchant category code) 2 bytes\n" +
                "8D (CDOL 2 - Card risk management data object list 2): 910A8A0295059F37049F4C08\n" +
                "  91 (issuer authentication data) 10 bytes\n" +
                "  8A (authorisation response code) 2 bytes\n" +
                "  95 (TVR - Terminal Verification Results) 5 bytes\n" +
                "  9F37 (unpredictable number) 4 bytes\n" +
                "  9F4C (ICC dynamic number) 8 bytes\n" +
                "90 (issuer public key certificate): 83E28958C1C1A1AA31DD4611C3B3B1FCBEE937B92A7ED93AE49D9510028907C4F5A685AF2A100FA347282F1BB3ADF26E2CB495080B20AE4A5614A9DF6602764177FBEE38624489AC6C4C054EAFC305D895A76885CE53A1BFDF5CB6CCE42E8E0E12382DB606490EE8075BC69D90205614B27745F7D883DB449758C340888B47B947945F062348A4DD4636D75A5D421638B6CA86B0618005C9F8F5D59DED345130123F0BD8C1EF7EC6F32A38BD0421FC39\n" +
                "92 (issuer public key remainder): 24A72C60B6F51D90F08F422DBA97429B21017F8080A05933C0A4D70728BD68E665CD19E1\n" +
                "9F8410 (?): 9291EA7DB1EA276A8C96999D\n" +
                "F5 (?): A69F86109C7C13CB1EBCD8C38818CB4EE91D\n" +
                "A1 (?): 9F8810FC28203B337296C500C3A272DA4767569F8A10253BC17EE2DE49451157405E9EA8C78D9F8503D119AC9F8703E6DF7C9F8903E08A739F8B0345C962DF2008ABE7D77B1E6970CB9F1101019F120A4D41535445524341\n" +
                "52 (?): 9F38039F1A029F49039F37049F4401028407A0000000041010BF0C059F4D020B0A9F2301009F1401009F42020643C303000000C403195000C50319FB00C7012DC8020643\n" +
                "C9 (?): 0643\n" +
                "CA (?): 000000000000\n" +
                "CB (?): 000000000000\n" +
                "D1 (?): 06430000000643000000064300000006430000000643000000\n" +
                "D3 (?): 000000000000000000000000000000000000\n" +
                "D5 (?): 04180200\n" +
                "D6 (?): 0010\n" +
                "9F7E (?): 031021120002000000000000000000000000000000000000000000000000000000000000000000000000000000000000\n" +
                "56 (?): 42353432303438353533373738383135345E202F5E31353038323231303130303030303030303030303030303030303030303030\n" +
                "9F62 (?): 00000000000E\n" +
                "9F63 (?): 00000000FE00\n" +
                "9F64 (?): 05\n" +
                "9F65 (?): 000E\n" +
                "9F66 (?): 07F0\n" +
                "9F67 (?): 05\n" +
                "9F6B (?): 5420485537788154D15082210100000000000F\n" +
                "D7 (?): 000024E0\n" +
                "D8 (?): 1980\n" +
                "D9 (?): 0801010010010301\n" +
                "DA (?): 0000\n" +
                "DB (?): 0000\n" +
                "DC (?): 1E82\n" +
                "DD (?): EA12\n" +
                "Error: Failed parsing 9F8C81B0A817\n";

        String tlv = "5F2002202F5F24031508315F2D047275656E5F280206438F01059F3201034F07A0000000041010500A4D415354455243415244870101820200809F080200025F300202215F25031411015A0854204855377881545F34010157135420485537788154D15082211839409890000F9F1F183138333934303030303030303030303938393030303030309F4A01828C249F02069F03069F1A0295055F2A029A039C019F37049F35019F45029F4C089F34039F15028D0C910A8A0295059F37049F4C089081B083E28958C1C1A1AA31DD4611C3B3B1FCBEE937B92A7ED93AE49D9510028907C4F5A685AF2A100FA347282F1BB3ADF26E2CB495080B20AE4A5614A9DF6602764177FBEE38624489AC6C4C054EAFC305D895A76885CE53A1BFDF5CB6CCE42E8E0E12382DB606490EE8075BC69D90205614B27745F7D883DB449758C340888B47B947945F062348A4DD4636D75A5D421638B6CA86B0618005C9F8F5D59DED345130123F0BD8C1EF7EC6F32A38BD0421FC39922424A72C60B6F51D90F08F422DBA97429B21017F8080A05933C0A4D70728BD68E665CD19E19F84100C9291EA7DB1EA276A8C96999DF512A69F86109C7C13CB1EBCD8C38818CB4EE91DA1589F8810FC28203B337296C500C3A272DA4767569F8A10253BC17EE2DE49451157405E9EA8C78D9F8503D119AC9F8703E6DF7C9F8903E08A739F8B0345C962DF2008ABE7D77B1E6970CB9F1101019F120A4D4153544552434152449F38039F1A029F49039F37049F4401028407A0000000041010BF0C059F4D020B0A9F2301009F1401009F42020643C303000000C403195000C50319FB00C7012DC8020643C9020643CA06000000000000CB06000000000000D11906430000000643000000064300000006430000000643000000D312000000000000000000000000000000000000D50404180200D60200109F7E30031021120002000000000000000000000000000000000000000000000000000000000000000000000000000000000000563442353432303438353533373738383135345E202F5E313530383232313031303030303030303030303030303030303030303030309F620600000000000E9F630600000000FE009F6401059F6502000E9F660207F09F6701059F6B135420485537788154D15082210100000000000FD704000024E0D8021980D9080801010010010301DA020000DB020000DC021E82DD02EA129F8C81B0A817FE4C94B779BC045870E11E1B233F5B2A119FBE8DB13D4A16DD70633AC2DA375536107D3DE7264ABBAA6AA5B25C221C9F6E3FEE038287492820434F40A70B9D99F0CEF237714FC85650A0352CF9CA4A31B7D9FC1703AAF4B00619E603A69D0A99B8AB2A59E8348F2057E434BD6A51AA087DBDDE7E61FC67D74D90F9E3D9A795889C05A84B60E531A1144001DA7B958EC8631BC3E358DA2352E094A5F835D63B9184D8D7EA807C4EFB55E233FD07A79F4701039F48009F6C0200019F83820118ACFB1B0537769E3B87AED410119795D14438EE3163B148827AFC63A669E6AE29DAF65E8A4915600817E3FFD31471A08C8C7C7E23A938E9714EA058257F5738E48408DC46ACEA06C71E83537F65C53C1C5BA6A57ACD6CB20E9B510D8AF7F002F2D3F62B2B14B980709845BAC035B74142E32C571DACF8F1482A93132F6C401E3071A56864FF2BC1F94F4B0008EF373FE556647BDF6CD6A8897B82C22AA9E4F37F1E590B981C50F7B12762E8B16DB336D8F338582E32EF720C3186CA6EE2B306E4E81CF6565A0F498B6E7AB6A90BCE02881692063F1E5B7E7BACECA95D83C065DBC0E96881584F60014FBEE12CA7ECE65D4CFA07D9D0BCF721FC308CB0CBB900D743F7EAA99B0723E8FA5F9F18B565C83948CEC67B685F6FE88E0E000000000000000042031E031F039F0702FF009F0D05B4508400009F0E0500000000009F0F05B4708480009F1701039F690F9F6A049F7E019F02065F2A029F1A029F4D020B0A9F8D024E20DF11020100DF1202C129DF14020000DF15020000DF16020643DF171906430000000643000000064300000006430000000643000000DF1806000000000000DF1906000000000000DF5502001E9404200101009F8E024E20DF4F020000DF5A03000000DF5B03000000DF5903000000DF5C03060000DF5E03005000DF4503000000DF5D03000000DF460300A000DF1A020100DF1B02E129DF1D020000DF1E020000DF1F0100DF210100DF4D02001EDF490100DF4A02001E9F6E07064300003039009F5D03000400DF600100DF610100DF480100DF560100DF5F0100DF4C1000112233445566778899AABBCCDDEEFFDF501000000000000000000000000000000000DF53140000000000000000000000000000000000000000DF5706000000000000DF5801009F2A0102DF43020000DF440200009F51039F3704";
        List<DecodedData> decoded = rootDecoder.decode(tlv, "EMV", "constructed");
        assertThat(decoded, decodedAsString(expected));
    }

    @Test
    public void handleOddDdol() throws Exception {
        String expected = "DF5D (Default DDOL - Default dynamic data auth object list): 000000\n" +
                "  Error: Failed parsing 000000: BufferUnderflowException\n";
        String tlv = "DF5D03000000";
        List<DecodedData> decoded = rootDecoder.decode(tlv, "EMV", "constructed");
        assertThat(decoded, decodedAsString(expected));
    }

    private List<DecodedData> decodeApdus(String apdus) {
        return rootDecoder.decode(apdus, "EMV", "apdu-sequence");
    }
}