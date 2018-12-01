package io.github.binaryfoo.decoders.apdu

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.EmvTags
import io.github.binaryfoo.decoders.DecodeSession
import io.github.binaryfoo.decoders.PopulatedDOLDecoder
import java.util.logging.Logger

class InternalAuthenticateAPDUDecoder : CommandAPDUDecoder {
  override fun getCommand(): APDUCommand {
    return APDUCommand.InternalAuthenticate
  }

  override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): DecodedData {
    val length = Integer.parseInt(input.substring(8, 10), 16)
    val data = input.substring(10, 10 + length * 2)
    val ddolValues = decodeDDOLElements(session, data, startIndexInBytes + 5)
    return DecodedData.constructed("C-APDU: Internal Authenticate", data, startIndexInBytes, startIndexInBytes + 6 + length, ddolValues)
  }

  private fun decodeDDOLElements(session: DecodeSession, populatedDdol: String, startIndexInBytes: Int): List<DecodedData> {
    val cdol = session[EmvTags.DDOL]
    if (cdol != null) {
      try {
        return PopulatedDOLDecoder().decode(cdol, populatedDdol, startIndexInBytes, session)
      } catch (e: Exception) {
        LOG.throwing(GenerateACAPDUDecoder::class.java.simpleName, "decodeDDOLElements", e)
      }

    }
    return listOf()
  }

  companion object {
    private val LOG = Logger.getLogger(InternalAuthenticateAPDUDecoder::class.java.name)
  }

}
