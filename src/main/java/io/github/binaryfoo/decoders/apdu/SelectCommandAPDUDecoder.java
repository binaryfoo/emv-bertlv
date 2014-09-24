package io.github.binaryfoo.decoders.apdu;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.decoders.DecodeSession;
import io.github.binaryfoo.tlv.ISOUtil;

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
