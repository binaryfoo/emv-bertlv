package com.willcurrie.decoders;

import com.willcurrie.DecodedData;
import com.willcurrie.Decoder;

public interface CommandAPDUDecoder {
    APDUCommand getCommand();

    DecodedData decode(String input, int startIndexInBytes);
}
