package io.github.binaryfoo.decoders.apdu

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.Decoder
import io.github.binaryfoo.HexDumpFactory
import io.github.binaryfoo.decoders.DecodeSession

import java.util.ArrayList

public class APDUSequenceDecoder(private val replyDecoder: ReplyAPDUDecoder, vararg commandDecoders: CommandAPDUDecoder) : Decoder {
    private val _commandDecoders: Array<CommandAPDUDecoder> = array(*commandDecoders)
    private val hexDumpFactory = HexDumpFactory()

    override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): List<DecodedData> {
        var runningStartIndexInBytes = startIndexInBytes
        val list = ArrayList<DecodedData>()
        input.split("\\s+").forEach { line ->
            try {
                val commandDecoder = getCommandDecoder(line)
                val decoded: DecodedData
                if (commandDecoder != null) {
                    session.setCurrentCommand(commandDecoder.getCommand())
                    decoded = commandDecoder.decode(line, runningStartIndexInBytes, session)
                } else {
                    decoded = replyDecoder.decode(line, runningStartIndexInBytes, session)
                }
                decoded.hexDump = hexDumpFactory.splitIntoByteLengthStrings(line, runningStartIndexInBytes)
                runningStartIndexInBytes = decoded.endIndex
                list.add(decoded)
            } catch (e: Exception) {
                list.add(DecodedData.primitive(line, "Failed to decode: " + e.getMessage(), 0, 0))
            }
        }
        return list
    }

    private fun getCommandDecoder(input: String): CommandAPDUDecoder? {
        val command = APDUCommand.fromHex(input.substring(0, 4))
        return _commandDecoders.firstOrNull { it.getCommand() == command }
    }

    override fun validate(input: String): String? {
        return null
    }

    override fun getMaxLength(): Int {
        return Integer.MAX_VALUE
    }
}
