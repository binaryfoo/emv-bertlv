package com.willcurrie.decoders;

import com.willcurrie.DecodedData;

public class GetProcessingOptionsCommandAPDUDecoder implements CommandAPDUDecoder {
    @Override
    public APDUCommand getCommand() {
        return APDUCommand.GetProcessingOptions;
    }

    @Override
    public DecodedData decode(String input, int startIndexInBytes) {
        int length = Integer.parseInt(input.substring(8, 10), 16);
        String pdol = input.substring(10, 10 + length * 2);
        return new DecodedData("C-APDU: GPO", "PDOL " + pdol, startIndexInBytes, startIndexInBytes + 5 + length + 1);
    }
}
