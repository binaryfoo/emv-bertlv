package io.github.binaryfoo.decoders.apdu;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.decoders.DecodeSession;

import java.util.Arrays;
import java.util.List;

public class ExternalAuthenticateAPDUDecoder implements CommandAPDUDecoder {
    @Override
    public APDUCommand getCommand() {
        return APDUCommand.ExternalAuthenticate;
    }

    @Override
    public DecodedData decode(String input, int startIndexInBytes, DecodeSession session) {
        int length = Integer.parseInt(input.substring(8, 10), 16);
        String data = input.substring(10, 10 + length * 2);
        return new DecodedData("C-APDU: External Authenticate", data, startIndexInBytes, startIndexInBytes + 5 + length, decodePayload(data, startIndexInBytes + 5));
    }

    // EMV v4.3 Book 3, 6.5.4.3 Data Field Sent in the Command Message
    private List<DecodedData> decodePayload(String data, int startIndexInBytes) {
        return Arrays.asList(
                new DecodedData("ARPC", data.substring(0, 16), startIndexInBytes, startIndexInBytes + 8),
                new DecodedData("Issuer Specific", data.substring(16), startIndexInBytes + 8, startIndexInBytes + 8 + data.substring(16).length()/2)
        );
    }
}
