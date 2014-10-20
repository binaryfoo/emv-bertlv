package io.github.binaryfoo.decoders

import java.util.Arrays

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.Decoder
import io.github.binaryfoo.tlv.ISOUtil

public class CVMResultsDecoder : Decoder {

    override fun decode(input: String, startIndexInBytes: Int, decodeSession: DecodeSession): List<DecodedData> {
        val rule = CVRule(input.substring(0, 4))
        val result = input.substring(4, 6)
        return Arrays.asList<DecodedData>(DecodedData.primitive(input.substring(0, 2), rule.getVerificationMethodDescription(), startIndexInBytes, startIndexInBytes + 1), DecodedData.primitive(input.substring(2, 4), rule.getConditionCodeDescription(), startIndexInBytes + 1, startIndexInBytes + 2), DecodedData.primitive(result, decodeResult(result), startIndexInBytes + 2, startIndexInBytes + 3))
    }

    private fun decodeResult(result: String): String {
        return if ("01" == result)
            "Failed"
        else if ("02" == result) "Sucessful" else "Unknown"
    }

    override fun getMaxLength(): Int {
        return FIELD_LENGTH
    }

    override fun validate(bitString: String?): String? {
        if (bitString == null || bitString.length() != FIELD_LENGTH) {
            return "Value must be exactly ${FIELD_LENGTH} characters"
        }
        if (!ISOUtil.isValidHexString(bitString)) {
            return "Value must contain only the characters 0-9 and A-F"
        }
        return null
    }

    class object {
        private val FIELD_LENGTH = 6
    }

}
