package com.willcurrie.decoders;

import com.willcurrie.DecodedData;

public class ExternalAuthenticateAPDUDecoder implements CommandAPDUDecoder {
    @Override
    public APDUCommand getCommand() {
        return APDUCommand.ExternalAuthenticate;
    }

    @Override
    public DecodedData decode(String input, int startIndexInBytes) {
        int length = Integer.parseInt(input.substring(8, 10));
        String data = input.substring(10, length * 2);
        return new DecodedData("C-APDU: External Authenticate", data, startIndexInBytes, 5 + length);
    }
}
