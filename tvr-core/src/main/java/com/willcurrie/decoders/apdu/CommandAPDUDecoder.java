package com.willcurrie.decoders.apdu;

import com.willcurrie.DecodedData;
import com.willcurrie.decoders.DecodeSession;

public interface CommandAPDUDecoder {
    APDUCommand getCommand();

    DecodedData decode(String input, int startIndexInBytes, DecodeSession session);
}
