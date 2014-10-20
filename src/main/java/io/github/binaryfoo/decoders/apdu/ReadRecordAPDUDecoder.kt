package io.github.binaryfoo.decoders.apdu

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.decoders.DecodeSession

public class ReadRecordAPDUDecoder : CommandAPDUDecoder {
    override fun getCommand(): APDUCommand {
        return APDUCommand.ReadRecord
    }

    override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): DecodedData {
        val recordNumber = input.substring(4, 6)
        val referenceControlParameter = Integer.parseInt(input.substring(6, 8), 16)
        val sfi = (referenceControlParameter and 0x000000f8) shr 3
        return DecodedData.primitive("C-APDU: Read Record", "number " + recordNumber + " SFI " + sfi, startIndexInBytes, startIndexInBytes + 5)
    }
}
