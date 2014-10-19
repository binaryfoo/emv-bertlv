package io.github.binaryfoo.decoders.apdu;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.decoders.DecodeSession;
import io.github.binaryfoo.tlv.Tag;

public class GetDataAPDUDecoder implements CommandAPDUDecoder {
    @Override
    public APDUCommand getCommand() {
        return APDUCommand.GetData;
    }

    @Override
    public DecodedData decode(String input, int startIndexInBytes, DecodeSession session) {
        String tagHex = input.substring(4, 8);
        Tag tag = Tag.fromHex(tagHex);
        return DecodedData.primitive("C-APDU: GetData", tag.toString(session.getTagMetaData()), startIndexInBytes, startIndexInBytes + 5);
    }
}
