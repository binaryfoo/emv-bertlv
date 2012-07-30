package com.willcurrie.decoders.apdu;

import com.willcurrie.DecodedData;
import com.willcurrie.decoders.DecodeSession;

public class ExternalAuthenticateAPDUDecoder implements CommandAPDUDecoder {
    @Override
    public APDUCommand getCommand() {
        return APDUCommand.ExternalAuthenticate;
    }

    @Override
    public DecodedData decode(String input, int startIndexInBytes, DecodeSession session) {
        int length = Integer.parseInt(input.substring(8, 10), 16);
        String data = input.substring(10, 10 + length * 2);
        return new DecodedData("C-APDU: External Authenticate", data, startIndexInBytes, startIndexInBytes + 5 + length);
    }
}
