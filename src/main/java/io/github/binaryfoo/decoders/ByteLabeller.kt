package io.github.binaryfoo.decoders

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.Decoder
import io.github.binaryfoo.bit.*
import io.github.binaryfoo.bit.EmvBit

import java.util.ArrayList

/**
 * Decode and label bits in a string according to the EMV spec convention.
 */
public class ByteLabeller : Decoder {
    override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): List<DecodedData> {
        val decoded = ArrayList<DecodedData>()
        for (bit in fromHex(input)) {
            val byteIndex = startIndexInBytes + bit.byteNumber - 1
            decoded.add(DecodedData.primitive(bit.toString(), "", byteIndex, byteIndex + 1))
        }
        return decoded
    }

    override fun validate(input: String?): String? {
        if (input != null && (input.length() % 2) != 0) {
            return "Must be an even number of characters"
        }
        return null
    }

    override fun getMaxLength(): Int {
        return Integer.MAX_VALUE
    }
}
