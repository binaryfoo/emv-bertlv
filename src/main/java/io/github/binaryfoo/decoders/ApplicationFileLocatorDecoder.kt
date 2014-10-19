package io.github.binaryfoo.decoders

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.Decoder

import java.util.ArrayList

/**
 * EMV Book 3 (v4.3) 10.2 Read Application Data
 */
public class ApplicationFileLocatorDecoder : Decoder {

    override fun decode(input: String, startIndexInBytes: Int, decodeSession: DecodeSession): List<DecodedData> {
        val decoded = ArrayList<DecodedData>()
        for (offset in 0..input.length-1 step 8) {
            val element = input.substring(offset, offset + 8)
            val sfi = Integer.parseInt(element.substring(0, 2), 16) shr 3
            val firstRecord = Integer.parseInt(element.substring(2, 4))
            val lastRecord = Integer.parseInt(element.substring(4, 6))
            val range = firstRecord.toString() + (if (lastRecord == firstRecord) "" else "-" + lastRecord)
            decoded.add(DecodedData.primitive("", "SFI " + sfi + " number " + range, startIndexInBytes + (offset / 2), startIndexInBytes + (offset / 2) + 4))
        }
        return decoded
    }

    override fun validate(input: String): String? {
        return null
    }

    override fun getMaxLength(): Int {
        return 0
    }
}
