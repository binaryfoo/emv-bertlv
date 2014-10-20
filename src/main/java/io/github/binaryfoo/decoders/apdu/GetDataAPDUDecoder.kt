package io.github.binaryfoo.decoders.apdu

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.decoders.DecodeSession
import io.github.binaryfoo.tlv.Tag

public class GetDataAPDUDecoder : CommandAPDUDecoder {
    override fun getCommand(): APDUCommand {
        return APDUCommand.GetData
    }

    override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): DecodedData {
        val tagHex = input.substring(4, 8)
        val tag = Tag.fromHex(tagHex)
        return DecodedData.primitive("C-APDU: GetData", tag.toString(session.getTagMetaData()), startIndexInBytes, startIndexInBytes + 5)
    }
}
