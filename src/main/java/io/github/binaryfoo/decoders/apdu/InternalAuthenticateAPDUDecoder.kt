package io.github.binaryfoo.decoders.apdu

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.decoders.DecodeSession

public class InternalAuthenticateAPDUDecoder : CommandAPDUDecoder {
    override fun getCommand(): APDUCommand {
        return APDUCommand.InternalAuthenticate
    }

    override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): DecodedData {
        val length = Integer.parseInt(input.substring(8, 10), 16)
        val data = input.substring(10, 10 + length * 2)
        return DecodedData.primitive("C-APDU: Internal Authenticate", data, startIndexInBytes, startIndexInBytes + 6 + length)
    }
}
