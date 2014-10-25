package io.github.binaryfoo.decoders.apdu

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.decoders.DecodeSession

/**
 * Read a record (bucket of bits) from the card's file system. The record is identified by the pair (SFI, record number).
 *
 * SFI being the "short" file indicator. Short in that it's only meaningful to the card's file system when combined with an application id (provided by the terminal selecting the app).
 * In this language each file has multiple records. Hence the record id. File names in a typical desktop OS are somewhat more helpful to people but since we're dealing with a
 * machine to machine conversation here numbers make sense.
 */
public class ReadRecordAPDUDecoder : CommandAPDUDecoder {
    override fun getCommand(): APDUCommand {
        return APDUCommand.ReadRecord
    }

    override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): DecodedData {
        val recordNumber = Integer.parseInt(input.substring(4, 6), 16)
        val referenceControlParameter = Integer.parseInt(input.substring(6, 8), 16)
        val sfi = (referenceControlParameter and 0x000000f8) shr 3
        return DecodedData.primitive("C-APDU: Read Record", "SFI ${sfi} record ${recordNumber}", startIndexInBytes, startIndexInBytes + 5)
    }
}
