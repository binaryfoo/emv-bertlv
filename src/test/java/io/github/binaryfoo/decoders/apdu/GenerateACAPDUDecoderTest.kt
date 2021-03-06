package io.github.binaryfoo.decoders.apdu

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.EmvTags
import io.github.binaryfoo.decoders.DecodeSession
import io.github.binaryfoo.decoders.annotator.backgroundOf
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class GenerateACAPDUDecoderTest {

  @Test
  fun testDecode() {
    val input = "80AE50002B00000001000100000000000000020000000000003612031500000EEC8522000000000000000000005E030000"
    val session = DecodeSession()
    session.tagMetaData = EmvTags.METADATA
    session.put(EmvTags.CDOL_1, "9F02069F03069F090295055F2A029A039C019F37049F35019F45029F4C089F3403")
    val startIndex = 6
    val decoded = GenerateACAPDUDecoder().decode(input, startIndex, session)

    assertThat(decoded.rawData, `is`("C-APDU: Generate AC (TC+CDA)"))
    assertThat(decoded.children.first(), `is`(DecodedData(EmvTags.AMOUNT_AUTHORIZED, "9F02 (amount authorized)", "000000010001", startIndex + 5, startIndex + 11)))

    val expectedDecodedCVMResults = EmvTags.METADATA.get(EmvTags.CVM_RESULTS).decoder.decode("5E0300", startIndex + 45, DecodeSession())
    assertThat(decoded.children.last(), `is`(DecodedData(EmvTags.CVM_RESULTS, "9F34 (CVM Results - Cardholder Verification Results)", "5E0300", startIndex + 45, startIndex + 48, expectedDecodedCVMResults, backgroundOf("How was the card holder verified? Did this check succeed?"))))
  }

  @Test
  fun testDecodeSecondGenerateAC() {
    val first = "80AE80002B000000001000000000000000000280000080000036120316008221F60122000000000000000000005E030000"
    val second = "80AE40001D11223344556677880000303080000080008221F601000000000000000000"
    val session = DecodeSession()
    session.tagMetaData = EmvTags.METADATA
    session.put(EmvTags.CDOL_1, "9F02069F03069F090295055F2A029A039C019F37049F35019F45029F4C089F3403")
    session.put(EmvTags.CDOL_2, "910A8A0295059F37049F4C08")

    val firstDecoded = GenerateACAPDUDecoder().decode(first, 0, session)
    assertThat(firstDecoded.rawData, `is`("C-APDU: Generate AC (ARQC)"))

    val secondDecoded = GenerateACAPDUDecoder().decode(second, 0, session)
    assertThat(secondDecoded.rawData, `is`("C-APDU: Generate AC (TC)"))
    assertThat(secondDecoded.children.first(), `is`(DecodedData(EmvTags.ISSUER_AUTHENTICATION_DATA, "91 (issuer authentication data)", "11223344556677880000", 5, 15)))
    assertThat(secondDecoded.children.last(), `is`(DecodedData(EmvTags.ICC_DYNAMIC_NUMBER, "9F4C (ICC dynamic number)", "0000000000000000", 26, 34, backgroundReading = backgroundOf("Nonce generated by the chip"))))
  }
}
