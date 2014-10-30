package io.github.binaryfoo.cmdline

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.RootDecoder
import io.github.binaryfoo.TagInfo
import io.github.binaryfoo.decoders.DecodeSession

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import kotlin.platform.platformStatic

public class Main {
    class object {
        platformStatic public fun main(args: Array<String>) {
            if (args.size < 2) {
                printHelp()
                System.exit(1)
            }
            val tag = args[0]
            val value = args[1]
            val meta = if (args.size > 2) args[2] else "EMV"

            val rootDecoder = RootDecoder()
            val decodeSession = DecodeSession()
            decodeSession.tagMetaData = rootDecoder.getTagMetaData(meta)
            val tagInfo = RootDecoder.getTagInfo(tag)
            if (tagInfo == null) {
                println("Unknown tag ${tag}")
                printHelp();
            } else {
                if (value == "-") {
                    val reader = BufferedReader(InputStreamReader(System.`in`))
                    while (true) {
                        val line = reader.readLine()
                        if (line == null) {
                            break
                        }
                        decodeValue(line, decodeSession, tagInfo)
                    }
                } else {
                    decodeValue(value, decodeSession, tagInfo)
                }
            }
        }

        private fun decodeValue(value: String, decodeSession: DecodeSession, tagInfo: TagInfo) {
            val decoded = tagInfo.decoder.decode(value, 0, decodeSession)
            DecodedWriter(System.out).write(decoded, "")
        }

        private fun printHelp() {
            System.out.println("Usage Main <decode-type> <value> [<tag-set>]")
            System.out.println("  <decode-type> is one of")
            for (tag in RootDecoder.getSupportedTags()) {
                System.out.println("    " + tag.getKey() + ": " + tag.getValue().shortName)
            }
            System.out.println("  <value> is the hex string or '-' for standard input")
            System.out.println("  <tag-set> is one of " + RootDecoder.getAllTagMeta() + " defaults to EMV")
        }
    }
}

fun main(args: Array<String>) = Main.main(args)
