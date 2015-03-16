package io.github.binaryfoo.decoders

import kotlin.properties.Delegates

public class UpiIdTypeDecoder : PrimitiveDecoder {

    override fun decode(hexString: String): String {
        return numericToName[hexString] ?: "Unknown"
    }

    class object {

        private val numericToName: Map<String, String> by Delegates.blockingLazy {
            csvToMap("upi-id-types.csv", 2)
        }
    }
}

