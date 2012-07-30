package com.willcurrie.decoders.apdu;

import com.willcurrie.DecodedData;
import com.willcurrie.decoders.DecodeSession;
import com.willcurrie.tlv.ISOUtil;

public class SelectCommandAPDUDecoder implements CommandAPDUDecoder {

    @Override
    public APDUCommand getCommand() {
        return APDUCommand.Select;
    }
    
    @Override
    public DecodedData decode(String input, int startIndexInBytes, DecodeSession session) {
        int length = Integer.parseInt(input.substring(8, 10), 16);
        String name = input.substring(10, 10 + length * 2);
        if (name.startsWith("A0")) {
            name = "AID " + name;
        } else {
            name = new String(ISOUtil.hex2byte(name));
        }
        return new DecodedData("C-APDU: Select",  name, startIndexInBytes, startIndexInBytes + 5 + length + 1);
    }

}
