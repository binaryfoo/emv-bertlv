package io.github.binaryfoo.decoders.apdu

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.decoders.DecodeSession

public trait CommandAPDUDecoder {
    public fun getCommand(): APDUCommand

    public fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): DecodedData
}
