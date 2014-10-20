package io.github.binaryfoo.decoders.apdu

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.decoders.DecodeSession

public class PutDataAPDUDecoder : CommandAPDUDecoder {
    override fun getCommand(): APDUCommand {
        return APDUCommand.PutData
    }

    override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): DecodedData {
        val length = Integer.parseInt(input.substring(8, 10), 16)
        return DecodedData.primitive("C-APDU: Put Data", "", startIndexInBytes, startIndexInBytes + 5 + length)
    }
}
