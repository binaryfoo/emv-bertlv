package com.willcurrie.decoders;

import com.willcurrie.tlv.ISOUtil;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ICCPublicKeyDecoderTest {
    @Test
    public void testDecode() throws Exception {
        byte[] recovered = ISOUtil.hex2byte("6A045413330089600044FFFF121400000101019003A028E99BECB507C507243C2E8DF4FE56A0297CD0AE72E2CFA992A98C80788422DBE00A1395B1545B09D66CFAB9ECEAF413E3DFF8227BC80BF6DA7F142B32673C527BB79129B5965C0F5DC4C3732BE6FA284F2469CDC545CD8AF915D2DD4AF2E171F5D36D502C42D0D7519B1CA8D3C689B65CC775687F051B2849BC");
        int byteLengthOfIssuerModulus = 144;
        String expected = "Header: 6A\n" +
                "Format: 04\n" +
                "PAN: 5413330089600044FFFF\n" +
                "Expiry Date (MMYY): 1214\n" +
                "Serial number: 000001\n" +
                "Hash algorithm: 01\n" +
                "Public key algorithm: 01\n" +
                "Public key length: 90 (144)\n" +
                "Public key exponent length: 03\n" +
                "Public key: A028E99BECB507C507243C2E8DF4FE56A0297CD0AE72E2CFA992A98C80788422DBE00A1395B1545B09D66CFAB9ECEAF413E3DFF8227BC80BF6DA7F142B32673C527BB79129B5965C0F5DC4C3732BE6FA284F2469CDC545CD8AF915D2DD4AF2E171F5D36D502C\n" +
                "Hash: 42D0D7519B1CA8D3C689B65CC775687F051B2849\n" +
                "Trailer: BC\n";
        String actual = new ICCPublicKeyDecoder().decode(recovered, byteLengthOfIssuerModulus);
        assertThat(actual, is(expected));
    }
}
