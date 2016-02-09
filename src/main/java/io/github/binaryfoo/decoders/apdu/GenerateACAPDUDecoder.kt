package io.github.binaryfoo.decoders.apdu

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.EmvTags
import io.github.binaryfoo.decoders.DecodeSession
import io.github.binaryfoo.decoders.PopulatedDOLDecoder
import io.github.binaryfoo.tlv.ISOUtil
import java.util.logging.Logger

class GenerateACAPDUDecoder : CommandAPDUDecoder {

    override fun getCommand(): APDUCommand {
        return APDUCommand.GenerateAC
    }

    override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): DecodedData {
        val length = Integer.parseInt(input.substring(8, 10), 16)
        val cid = ISOUtil.hex2byte(input.substring(4, 6))[0]
        val cryptogramType = parseCryptogramType(cid)
        val cda = (cid.toInt() and 0x10) == 0x10
        val populatedCDOL = input.substring(10, 10 + length * 2)
        val decodedPopulatedCDOL = decodeCDOLElements(session, populatedCDOL, startIndexInBytes + 5)
        return DecodedData.constructed("C-APDU: Generate AC (" + cryptogramType + (if (cda) "+CDA" else "") + ")", "CDOL " + populatedCDOL, startIndexInBytes, startIndexInBytes + 5 + length + 1, decodedPopulatedCDOL)
    }

    private fun parseCryptogramType(b: Byte): String {
        if ((b.toInt() and 0x40) == 0x40) {
            return "TC"
        }
        if ((b.toInt() and 0x80) == 0x80) {
            return "ARQC"
        }
        return "AAC"
    }

    private fun decodeCDOLElements(session: DecodeSession, populatedCdol: String, startIndexInBytes: Int): List<DecodedData> {
        val cdol = findCDOL(session)
        if (cdol != null) {
            try {
                return PopulatedDOLDecoder().decode(cdol, populatedCdol, startIndexInBytes, session)
            } catch (e: Exception) {
                LOG.throwing(GenerateACAPDUDecoder::class.java.simpleName, "decodeCDOLElements", e)
            }

        }
        return listOf()
    }

    private fun findCDOL(session: DecodeSession): String? {
        if (session.firstGenerateACCommand) {
            session.firstGenerateACCommand = false
            return session[EmvTags.CDOL_1]
        } else {
            return session[EmvTags.CDOL_2]
        }
    }

    companion object {
        private val LOG = Logger.getLogger(GenerateACAPDUDecoder::class.java.name)
    }
}
