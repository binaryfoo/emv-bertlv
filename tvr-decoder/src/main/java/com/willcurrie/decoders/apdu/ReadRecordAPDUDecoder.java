package com.willcurrie.decoders.apdu;

import com.willcurrie.DecodedData;
import com.willcurrie.decoders.DecodeSession;

public class ReadRecordAPDUDecoder implements CommandAPDUDecoder {
    @Override
    public APDUCommand getCommand() {
        return APDUCommand.ReadRecord;
    }

    @Override
    public DecodedData decode(String input, int startIndexInBytes, DecodeSession session) {
        return new DecodedData("C-APDU: Read Record ", input.substring(4, 8), startIndexInBytes, startIndexInBytes + 5);
    }
}
