package io.github.binaryfoo.decoders

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.Decoder

import java.util.ArrayList
import java.util.Collections

public class IssuerApplicationDataDecoder : Decoder {

    override fun decode(input: String, startIndexInBytes: Int, decodeSession: DecodeSession): List<DecodedData> {
        try {
            // should do something better to tell the difference like use the session
            if (input.length() == 36 || input.length() == 52) {
                return decodeMChipIad(input, startIndexInBytes, decodeSession)
            }
            return decodeVisaIad(input, startIndexInBytes, decodeSession)
        } catch (ignored: Exception) {
        }

        return listOf()
    }

    /*
     * From Visa Contactless Payment Specification v2.1
     *
     * Also seems to apply for AMEX and JCB to some extent.
     */
    private fun decodeVisaIad(input: String, startIndexInBytes: Int, decodeSession: DecodeSession): List<DecodedData> {
        val decoded = ArrayList<DecodedData>()
        val length = Integer.parseInt(input.substring(0, 2), 16)
        val dki = input.substring(2, 4)
        decoded.add(DecodedData.primitive("Derivation key index", dki, startIndexInBytes + 1, startIndexInBytes + 2))
        val cvn = input.substring(4, 6)
        decoded.add(DecodedData.primitive("Cryptogram version number", cvn, startIndexInBytes + 2, startIndexInBytes + 3))
        val trueCvr = input.substring(6, 2 + length * 2)
        val decodableCvr = input.substring(6, 6 + 8) // until more specs about CVR
        decoded.add(DecodedData.constructed("Card verification results", trueCvr, startIndexInBytes + 3, startIndexInBytes + (6 + Math.min(8, length * 2)) / 2, VisaCardVerificationResultsDecoder().decode(decodableCvr, startIndexInBytes + 3, decodeSession)))
        if (input.length() > 2 + length * 2) {
            val iddLength = Integer.parseInt(input.substring(14, 16), 16)
            val endIndex = 16 + iddLength * 2
            if ((iddLength > 0) && (endIndex <= input.length())) {
                val idd = input.substring(16, endIndex)
                decoded.add(DecodedData.primitive("Issuer discretionary data", idd, startIndexInBytes + 8, startIndexInBytes + 1 + iddLength))
            }
        }
        return decoded
    }

    /*
     * From A.24 Issuer Application Data, M/Chip 4 Issuer Guide to Debit and Credit Parameter Management • December 2004
     * Probably a tad outdated...
     */
    private fun decodeMChipIad(input: String, startIndexInBytes: Int, decodeSession: DecodeSession): List<DecodedData> {
        val decoded = ArrayList<DecodedData>()
        val kdi = input.substring(0, 2) // like dki but different?
        decoded.add(DecodedData.primitive("Key Derivation index", kdi, startIndexInBytes, startIndexInBytes + 1))
        val cvn = input.substring(2, 4)
        decoded.add(DecodedData.primitive("Cryptogram version number", cvn, startIndexInBytes + 1, startIndexInBytes + 2))
        val cvr = input.substring(4, 16)
        decoded.add(DecodedData.constructed("Card verification results", cvr, startIndexInBytes + 2, startIndexInBytes + 8, MastercardCVRDecoder().decode(cvr, startIndexInBytes + 2, decodeSession)))
        val dac = input.substring(16, 20)
        decoded.add(DecodedData.primitive("DAC/ICC Dynamic Number 2 Bytes", dac, startIndexInBytes + 8, startIndexInBytes + 10))
        val counters = input.substring(20, 36)
        decoded.add(DecodedData.primitive("Plaintext/Encrypted Counters", counters, startIndexInBytes + 10, startIndexInBytes + 18))
        return decoded
    }

    override fun validate(input: String): String? {
        return null
    }

    override fun getMaxLength(): Int {
        return 0
    }
}
