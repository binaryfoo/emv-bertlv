package io.github.binaryfoo.decoders

import io.github.binaryfoo.tlv.ISOUtil
import org.junit.Test

import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat

public class SignedDynamicApplicationDataDecoderTest {

    Test
    public fun testDecode() {
        val recovered = ISOUtil.hex2byte("6A0501260836DF6D9E2104092E40D58B731AF5885C067BE29D015DD4C9454026810F0879E219B8A7DCD0BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB5734B62BE6BFF2A04C1CFF4060E549C932E1723DBC")
        val decoded = decodeSignedDynamicData(recovered, 144)
        assertThat(decoded, `is`("""Header: 6A
Format: 05
Hash algorithm: 01
Dynamic data length: 38
Dynamic data: 0836DF6D9E2104092E40D58B731AF5885C067BE29D015DD4C9454026810F0879E219B8A7DCD0""" +
//            08 36DF6D9E2104092E 40 D58B731AF5885C06 7BE29D015DD4C9454026810F0879E219B8A7DCD0
//               dynamic number      cryptogram       hash code
"""
Hash: 5734B62BE6BFF2A04C1CFF4060E549C932E1723D
Trailer: BC
"""))
    }
}
