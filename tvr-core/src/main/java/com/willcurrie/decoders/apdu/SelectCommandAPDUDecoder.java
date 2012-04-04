package com.willcurrie.decoders.apdu;

import com.willcurrie.DecodedData;
import com.willcurrie.decoders.DecodeSession;

public class SelectCommandAPDUDecoder implements CommandAPDUDecoder {

    @Override
    public APDUCommand getCommand() {
        return APDUCommand.Select;
    }
    
    @Override
    public DecodedData decode(String input, int startIndexInBytes, DecodeSession session) {
        int length = Integer.parseInt(input.substring(8, 10), 16);
        String aid = input.substring(10, 10 + length * 2);
        return new DecodedData("C-APDU: Select",  "AID " + aid, startIndexInBytes, startIndexInBytes + 5 + length + 1);
    }

}
