package io.github.binaryfoo.decoders

import io.github.binaryfoo.tlv.ISOUtil
import org.junit.Test

import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import io.github.binaryfoo.DecodedAsStringMatcher

class SignedDynamicApplicationDataDecoderTest {

    @Test fun decodeOutputOfCDA() {
        val recovered = ISOUtil.hex2byte("6A0501260836DF6D9E2104092E40D58B731AF5885C067BE29D015DD4C9454026810F0879E219B8A7DCD0BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB5734B62BE6BFF2A04C1CFF4060E549C932E1723DBC")
        val decoded = decodeSignedDynamicData(recovered, 0)
        assertThat(decoded, `is`(DecodedAsStringMatcher.decodedAsString("""Header: 6A
Format: 05
Hash algorithm: 01
Dynamic data length: 38
ICC dynamic number length: 8
ICC dynamic number: 36DF6D9E2104092E
Cryptogram information data: 40
Cryptogram: D58B731AF5885C06
Transaction data hash code: 7BE29D015DD4C9454026810F0879E219B8A7DCD0
Hash: 5734B62BE6BFF2A04C1CFF4060E549C932E1723D
Trailer: BC
""")))
    }

    @Test fun decodeBogStandardDDA() {
        val recovered = ISOUtil.hex2byte("6A05010706112233445566BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB97C21EB1AA67291E00322913CE1C52CCF0D93200BC")
        val decoded = decodeSignedDynamicData(recovered, 0)
        assertThat(decoded, `is`(DecodedAsStringMatcher.decodedAsString("""Header: 6A
Format: 05
Hash algorithm: 01
Dynamic data length: 7
ICC dynamic number length: 6
ICC dynamic number: 112233445566
Hash: 97C21EB1AA67291E00322913CE1C52CCF0D93200
Trailer: BC
""")))
    }
}
