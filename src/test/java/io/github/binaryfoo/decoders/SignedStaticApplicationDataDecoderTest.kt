package io.github.binaryfoo.decoders

import io.github.binaryfoo.tlv.ISOUtil
import org.junit.Test

import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat

public class SignedStaticApplicationDataDecoderTest {
    Test
    public fun decode() {
        val recovered = ISOUtil.hex2byte("6A03010001BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBF6E1F5D3652F06C2D5F9E6599AE8ED5BE1D575CFBC")
        val notes = decodeSignedStaticData(recovered, 0)
        assertThat(notes, `is`("""Header: 6A
Format: 03
Hash Algorithm: 01
Data Auth Code: 0001
Hash: F6E1F5D3652F06C2D5F9E6599AE8ED5BE1D575CF
Trailer: BC
"""))
    }
}
