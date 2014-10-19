package io.github.binaryfoo.decoders

import java.util.ArrayList

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.Decoder
import io.github.binaryfoo.tlv.ISOUtil

/**
 * Decoder for Cardholder Verification Method List.
 * The list of ways to verify the person holding the card is authorized to use it.
 */
public class CVMListDecoder : Decoder {

    override fun decode(input: String, startIndexInBytes: Int, decodeSession: DecodeSession): MutableList<DecodedData>? {
        val x = Integer.parseInt(input.substring(0, 8), 16)
        val y = Integer.parseInt(input.substring(8, 16), 16)
        val decodedData = ArrayList<DecodedData>()
        for (i in LENGTH_OF_AMOUNT_FIELDS_IN_CHARACTERS..input.length-LENGTH_OF_CV_RULE step LENGTH_OF_CV_RULE) {
            val ruleAsHexString = input[i, i+LENGTH_OF_CV_RULE].toString()
            val rule = CVRule(ruleAsHexString)
            decodedData.add(DecodedData.primitive(ruleAsHexString, rule.getDescription(x, y), startIndexInBytes + i / 2, startIndexInBytes + (i + LENGTH_OF_CV_RULE) / 2))
        }
        return decodedData
    }

    override fun getMaxLength(): Int {
        return 1000
    }

    override fun validate(bitString: String?): String? {
        if (bitString == null || bitString.length() < LENGTH_OF_AMOUNT_FIELDS_IN_CHARACTERS) {
            return "Value must be at least ${LENGTH_OF_AMOUNT_FIELDS_IN_CHARACTERS} characters"
        }
        if (bitString.length() % LENGTH_OF_CV_RULE != 0) {
            return "Length must be a multiple of " + LENGTH_OF_CV_RULE
        }
        if (!ISOUtil.isValidHexString(bitString)) {
            return "Value must contain only the characters 0-9 and A-F"
        }
        return null
    }

}

private val LENGTH_OF_AMOUNT_FIELDS_IN_CHARACTERS = 16
private val LENGTH_OF_CV_RULE = 4
