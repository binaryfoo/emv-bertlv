package io.github.binaryfoo.decoders

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.Decoder

import java.util.ArrayList
import kotlin.text.substring

/**
 * EMV Book 3 (v4.3) 10.2 Read Application Data
 */
class ApplicationFileLocatorDecoder : Decoder {

    override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): List<DecodedData> {
        val decoded = ArrayList<DecodedData>()
        for (offset in 0..input.length-1 step 8) {
            val element = input.substring(offset, offset + 8)
            val sfi = Integer.parseInt(element.substring(0, 2), 16) shr 3
            val firstRecord = Integer.parseInt(element.substring(2, 4))
            val lastRecord = Integer.parseInt(element.substring(4, 6))
            val decoding = "SFI $sfi " + (if (lastRecord == firstRecord) "record $firstRecord" else "records $firstRecord-$lastRecord")
            decoded.add(DecodedData.primitive("", decoding, startIndexInBytes + (offset / 2), startIndexInBytes + (offset / 2) + 4))
        }
        return decoded
    }

    override fun validate(input: String?): String? {
        return null
    }

    override fun getMaxLength(): Int {
        return 0
    }
}
