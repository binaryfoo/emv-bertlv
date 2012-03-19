package com.willcurrie.decoders.apdu;

import com.willcurrie.DecodedData;

public interface CommandAPDUDecoder {
    APDUCommand getCommand();

    DecodedData decode(String input, int startIndexInBytes, DecodeSession session);
}
