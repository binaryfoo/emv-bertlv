package io.github.binaryfoo.decoders.apdu;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.decoders.DecodeSession;

public class GetChallengeAPDUDecoder implements CommandAPDUDecoder {
    @Override
    public APDUCommand getCommand() {
        return APDUCommand.GetChallenge;
    }

    @Override
    public DecodedData decode(String input, int startIndexInBytes, DecodeSession session) {
        return DecodedData.primitive("C-APDU: Get Challenge", "", startIndexInBytes, startIndexInBytes + 5);
    }
}
