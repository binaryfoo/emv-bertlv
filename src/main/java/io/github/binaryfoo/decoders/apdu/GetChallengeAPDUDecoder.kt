package io.github.binaryfoo.decoders.apdu

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.decoders.DecodeSession

public class GetChallengeAPDUDecoder : CommandAPDUDecoder {
    override fun getCommand(): APDUCommand {
        return APDUCommand.GetChallenge
    }

    override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): DecodedData {
        return DecodedData(null, "C-APDU: Get Challenge", "", startIndexInBytes, startIndexInBytes + 5)
    }
}
