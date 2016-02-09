package io.github.binaryfoo.decoders.apdu

import io.github.binaryfoo.decoders.DecodeSession
import io.github.binaryfoo.DecodedData
import kotlin.text.substring

class ReadBinaryAPDUDecoder : CommandAPDUDecoder {
    override fun getCommand(): APDUCommand {
        return APDUCommand.ReadBinary
    }

    override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): DecodedData {
        val p1 = Integer.parseInt(input.substring(4, 6), 16)
        val p2 = Integer.parseInt(input.substring(6, 8), 16)
        return DecodedData(null, "C-APDU: Read Binary", "P1=$p1 P2=$p2", startIndexInBytes, startIndexInBytes + 5)
    }
}

