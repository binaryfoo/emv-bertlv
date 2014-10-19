package io.github.binaryfoo.decoders.apdu;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.decoders.DecodeSession;

public class InternalAuthenticateAPDUDecoder implements CommandAPDUDecoder {
    @Override
    public APDUCommand getCommand() {
        return APDUCommand.InternalAuthenticate;
    }

    @Override
    public DecodedData decode(String input, int startIndexInBytes, DecodeSession session) {
        int length = Integer.parseInt(input.substring(8, 10), 16);
        String data = input.substring(10, 10 + length * 2);
        return DecodedData.primitive("C-APDU: Internal Authenticate", data, startIndexInBytes, startIndexInBytes + 6 + length);
    }
}
