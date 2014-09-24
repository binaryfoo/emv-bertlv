package io.github.binaryfoo.decoders.apdu;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.decoders.DecodeSession;

public interface CommandAPDUDecoder {
    APDUCommand getCommand();

    DecodedData decode(String input, int startIndexInBytes, DecodeSession session);
}
