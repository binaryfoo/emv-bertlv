package com.willcurrie.decoders;

import com.willcurrie.tlv.ISOUtil;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SignedDynamicApplicationDataDecoderTest {
    @Test
    public void testDecode() throws Exception {
        String expected = "Header: 6A\n" +
                "Format: 05\n" +
                "Hash algorithm: 01\n" +
                "Dynamic data length: 26 (38)\n" +
                "Dynamic data: 0836DF6D9E2104092E40D58B731AF5885C067BE29D015DD4C9454026810F0879E219B8A7DCD0\n" +
                "Hash: BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB\n" +
                "Trailer: BB\n";
        byte[] recovered = ISOUtil.hex2byte("6A0501260836DF6D9E2104092E40D58B731AF5885C067BE29D015DD4C9454026810F0879E219B8A7DCD0BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB5734B62BE6BFF2A04C1CFF4060E549C932E1723DBC");
        String decoded = new SignedDynamicApplicationDataDecoder().decode(recovered, 144);
        assertThat(decoded, is(expected));
        // dynamic data: 08 36DF6D9E2104092E 40 D58B731AF5885C06 7BE29D015DD4C9454026810F0879E219B8A7DCD0
        //                  dynamic number      cryptogram       hash code
    }
}
