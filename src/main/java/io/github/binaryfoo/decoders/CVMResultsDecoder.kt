package io.github.binaryfoo.decoders

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.Decoder
import io.github.binaryfoo.tlv.ISOUtil

class CVMResultsDecoder : Decoder {

  override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): List<DecodedData> {
    val rule = CVRule(input.substring(0, 4))
    val result = input.substring(4, 6)
    return listOf(
        DecodedData.primitive(input.substring(0, 2), rule.verificationMethodDescription, startIndexInBytes, startIndexInBytes + 1),
        DecodedData.primitive(input.substring(2, 4), rule.conditionCodeDescription, startIndexInBytes + 1, startIndexInBytes + 2),
        DecodedData.primitive(result, decodeResult(result), startIndexInBytes + 2, startIndexInBytes + 3))
  }

  private fun decodeResult(result: String): String {
    return if ("01" == result)
      "Failed"
    else if ("02" == result) "Sucessful" else "Unknown"
  }

  override fun getMaxLength(): Int = FIELD_LENGTH

  override fun validate(input: String?): String? {
    if (input == null || input.length != FIELD_LENGTH) {
      return "Value must be exactly $FIELD_LENGTH characters"
    }
    if (!ISOUtil.isValidHexString(input)) {
      return "Value must contain only the characters 0-9 and A-F"
    }
    return null
  }

  companion object {
    private val FIELD_LENGTH = 6
  }

}
