package io.github.binaryfoo.decoders;

import io.github.binaryfoo.tlv.ISOUtil;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SignedStaticApplicationDataDecoderTest {
    @Test
    public void decode() throws Exception {
        byte[] recovered = ISOUtil.hex2byte("6A03010001BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBF6E1F5D3652F06C2D5F9E6599AE8ED5BE1D575CFBC");
        String notes = new SignedStaticApplicationDataDecoder().decode(recovered, 0);
        assertThat(notes, is("Header: 6A\n" +
                "Format: 03\n" +
                "Hash Algorithm: 01\n" +
                "Data Auth Code: 0001\n" +
                "Hash: F6E1F5D3652F06C2D5F9E6599AE8ED5BE1D575CF\n" +
                "Trailer: BC\n"));
    }
}
