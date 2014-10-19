package io.github.binaryfoo.decoders.apdu;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.decoders.DecodeSession;

public class ComputeCryptoChecksumDecoder implements CommandAPDUDecoder{
    @Override
    public APDUCommand getCommand() {
        return APDUCommand.ComputeCryptographicChecksum;
    }

    @Override
    public DecodedData decode(String input, int startIndexInBytes, DecodeSession session) {
        int length = Integer.parseInt(input.substring(8, 10), 16);
        return DecodedData.primitive("C-APDU: Compute checksum", input.substring(10, 10 + length * 2), startIndexInBytes, startIndexInBytes + 5 + length + 1);
    }
}
