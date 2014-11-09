package io.github.binaryfoo.decoders.apdu

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.Decoder
import io.github.binaryfoo.HexDumpFactory
import io.github.binaryfoo.decoders.DecodeSession

import java.util.ArrayList
import io.github.binaryfoo.decoders.IssuerPublicKeyDecoder
import io.github.binaryfoo.EmvTags
import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.Decoder
import io.github.binaryfoo.HexDumpFactory
import io.github.binaryfoo.decoders.DecodeSession

import java.util.ArrayList
import io.github.binaryfoo.decoders.IssuerPublicKeyDecoder
import io.github.binaryfoo.EmvTags
import io.github.binaryfoo.decoders.SignedStaticApplicationDataDecoder
import io.github.binaryfoo.decoders.ICCPublicKeyDecoder
import io.github.binaryfoo.decoders.SignedDynamicApplicationDataDecoder
import io.github.binaryfoo.decoders.annotator
import io.github.binaryfoo.decoders.annotator.BackgroundReading

public class APDUSequenceDecoder(private val replyDecoder: ReplyAPDUDecoder, vararg commandDecoders: CommandAPDUDecoder) : Decoder {
    private val _commandDecoders: Array<CommandAPDUDecoder> = array(*commandDecoders)
    private val hexDumpFactory = HexDumpFactory()
    private val signedDataRecoverers = listOf(
        IssuerPublicKeyDecoder(),
        ICCPublicKeyDecoder(),
        SignedStaticApplicationDataDecoder(),
        SignedDynamicApplicationDataDecoder()
    )

    override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): List<DecodedData> {
        var runningStartIndexInBytes = startIndexInBytes
        val list = ArrayList<DecodedData>()
        input.toUpperCase().split("\\s+").forEach { line ->
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
                decoded.hexDump = hexDumpFactory.splitIntoByteLengthStrings(line, runningStartIndexInBytes)
                runningStartIndexInBytes = decoded.endIndex
                list.add(decoded)
            } catch (e: Exception) {
                list.add(DecodedData.primitive(line, "Failed to decode: " + e.getMessage(), 0, 0))
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
            } catch(e: Exception) {
            }
        }
    }
}
