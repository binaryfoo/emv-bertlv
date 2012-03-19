package com.willcurrie.decoders;

import com.willcurrie.DecodedData;
import com.willcurrie.tlv.BerTlv;
import com.willcurrie.tlv.ISOUtil;

public class GetProcessingOptionsCommandAPDUDecoder implements CommandAPDUDecoder {
    @Override
    public APDUCommand getCommand() {
        return APDUCommand.GetProcessingOptions;
    }

    @Override
    public DecodedData decode(String input, int startIndexInBytes) {
        int length = Integer.parseInt(input.substring(8, 10), 16);
        String pdol = input.substring(10, 10 + length * 2);
        BerTlv pdolTlv = BerTlv.parse(ISOUtil.hex2byte(pdol));
        return new DecodedData("C-APDU: GPO", "PDOL " + pdolTlv.getValueAsHexString(), startIndexInBytes, startIndexInBytes + 5 + length + 1);
    }
}
