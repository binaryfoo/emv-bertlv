package io.github.binaryfoo.decoders.apdu

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.EmvTags
import io.github.binaryfoo.decoders.DecodeSession
import io.github.binaryfoo.decoders.PopulatedDOLDecoder
import io.github.binaryfoo.tlv.BerTlv
import io.github.binaryfoo.tlv.ISOUtil

class GetProcessingOptionsCommandAPDUDecoder : CommandAPDUDecoder {
    override fun getCommand(): APDUCommand {
        return APDUCommand.GetProcessingOptions
    }

    override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): DecodedData {
        val length = Integer.parseInt(input.substring(8, 10), 16)
        val populatedPdol = input.substring(10, 10 + length * 2)
        val populatedPdolTlv = BerTlv.parse(ISOUtil.hex2byte(populatedPdol))
        val valueAsHexString = populatedPdolTlv.valueAsHexString
        val decodedPDOLElements = decodePDOLElements(session, valueAsHexString, startIndexInBytes + 5 + populatedPdolTlv.startIndexOfValue)
        val decodedData = if (populatedPdolTlv.getValue().size == 0) "No PDOL included" else "PDOL " + valueAsHexString
        return DecodedData.constructed("C-APDU: GPO", decodedData, startIndexInBytes, startIndexInBytes + 5 + length + 1, decodedPDOLElements)
    }

    private fun decodePDOLElements(session: DecodeSession, populatedPdol: String, startIndexInBytes: Int): List<DecodedData> {
        val pdol = session[EmvTags.PDOL]
        if (pdol != null) {
            return PopulatedDOLDecoder().decode(pdol, populatedPdol, startIndexInBytes, session)
        } else {
            return listOf()
        }
    }
}
