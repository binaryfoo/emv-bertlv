package io.github.binaryfoo.decoders.apdu

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.decoders.DecodeSession
import kotlin.text.substring

class ComputeCryptoChecksumDecoder : CommandAPDUDecoder {
    override fun getCommand(): APDUCommand {
        return APDUCommand.ComputeCryptographicChecksum
    }

    override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): DecodedData {
        val length = Integer.parseInt(input.substring(8, 10), 16)
        return DecodedData(null, "C-APDU: Compute checksum", input.substring(10, 10 + length * 2), startIndexInBytes, startIndexInBytes + 5 + length + 1)
    }
}
