package io.github.binaryfoo.decoders.apdu

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.decoders.DecodeSession
import io.github.binaryfoo.tlv.ISOUtil

public class SelectCommandAPDUDecoder : CommandAPDUDecoder {

    override fun getCommand(): APDUCommand {
        return APDUCommand.Select
    }

    override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): DecodedData {
        val length = Integer.parseInt(input.substring(8, 10), 16)
        var name = input.substring(10, 10 + length * 2)
        if (name.startsWith("A0")) {
            name = "AID " + name
        } else {
            name = String(ISOUtil.hex2byte(name))
        }
        return DecodedData.primitive("C-APDU: Select", name, startIndexInBytes, startIndexInBytes + 5 + length + 1)
    }

}
