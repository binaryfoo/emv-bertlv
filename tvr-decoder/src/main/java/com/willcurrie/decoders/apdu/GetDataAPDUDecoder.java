package com.willcurrie.decoders.apdu;

import com.willcurrie.DecodedData;
import com.willcurrie.tlv.Tag;

public class GetDataAPDUDecoder implements CommandAPDUDecoder {
    @Override
    public APDUCommand getCommand() {
        return APDUCommand.GetData;
    }

    @Override
    public DecodedData decode(String input, int startIndexInBytes, DecodeSession session) {
        String tagHex = input.substring(4, 8);
        Tag tag = Tag.fromHex(tagHex);
        return new DecodedData("C-APDU: GetData", tag.toString(), startIndexInBytes, startIndexInBytes + 7);
    }
}
