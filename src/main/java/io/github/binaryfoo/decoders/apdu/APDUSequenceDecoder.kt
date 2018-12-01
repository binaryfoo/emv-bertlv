package io.github.binaryfoo.decoders.apdu

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.Decoder
import io.github.binaryfoo.decoders.*
import io.github.binaryfoo.decoders.annotator.BackgroundReading
import io.github.binaryfoo.hex.HexDumpElement
import java.util.*

class APDUSequenceDecoder(private val replyDecoder: ReplyAPDUDecoder, vararg commandDecoders: CommandAPDUDecoder) : Decoder {
  private val _commandDecoders: Array<CommandAPDUDecoder> = arrayOf(*commandDecoders)
  private val signedDataRecoverers = listOf(
      IssuerPublicKeyDecoder(),
      ICCPublicKeyDecoder(),
      SignedStaticApplicationDataDecoder(),
      SignedDynamicApplicationDataDecoder()
  )

  override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): List<DecodedData> {
    var runningStartIndexInBytes = startIndexInBytes
    val list = ArrayList<DecodedData>()
    input.toUpperCase().split(Regex("\\s+")).filter { it.isNotBlank() }.forEach { line ->
      try {
        val commandDecoder = getCommandDecoder(line)
        val decoded: DecodedData
        if (commandDecoder != null) {
          session.currentCommand = commandDecoder.getCommand()
          decoded = commandDecoder.decode(line, runningStartIndexInBytes, session)
          decoded.category = "c-apdu"
          decoded.backgroundReading = BackgroundReading.readingFor(commandDecoder.getCommand())
        } else {
          decoded = replyDecoder.decode(line, runningStartIndexInBytes, session)
          decoded.category = "r-apdu"
        }
        decoded.hexDump = HexDumpElement.splitIntoByteLengthStrings(line, runningStartIndexInBytes)
        runningStartIndexInBytes = decoded.endIndex
        list.add(decoded)
      } catch (e: Exception) {
        list.add(DecodedData.primitive(line, "Failed to decode: " + e.message, 0, 0))
      }
    }
    postProcess(list, session)
    return list
  }

  private fun getCommandDecoder(input: String): CommandAPDUDecoder? {
    val command = APDUCommand.fromHex(input.substring(0, 4))
    return _commandDecoders.firstOrNull { it.getCommand() == command }
  }

  override fun validate(input: String?): String? {
    return null
  }

  override fun getMaxLength(): Int {
    return Integer.MAX_VALUE
  }

  fun postProcess(decoded: List<DecodedData>, session: DecodeSession) {
    for (processor in signedDataRecoverers) {
      try {
        processor.decodeSignedData(session, decoded)
      } catch (e: Exception) {
      }
    }
  }
}
