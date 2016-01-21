package io.github.binaryfoo.decoders.apdu

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.decoders.DecodeSession

import java.util.Arrays
import kotlin.collections.listOf
import kotlin.text.substring

public class ExternalAuthenticateAPDUDecoder : CommandAPDUDecoder {
    override fun getCommand(): APDUCommand {
        return APDUCommand.ExternalAuthenticate
    }

    override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): DecodedData {
        val length = Integer.parseInt(input.substring(8, 10), 16)
        val data = input.substring(10, 10 + length * 2)
        return DecodedData.constructed("C-APDU: External Authenticate", data, startIndexInBytes, startIndexInBytes + 5 + length, decodePayload(data, startIndexInBytes + 5))
    }

    // EMV v4.3 Book 3, 6.5.4.3 Data Field Sent in the Command Message
    private fun decodePayload(data: String, startIndexInBytes: Int): List<DecodedData> {
        return listOf(
                DecodedData.primitive("ARPC", data.substring(0, 16), startIndexInBytes, startIndexInBytes + 8),
                DecodedData.primitive("Issuer Specific", data.substring(16), startIndexInBytes + 8, startIndexInBytes + 8 + data.substring(16).length / 2))
    }
}
