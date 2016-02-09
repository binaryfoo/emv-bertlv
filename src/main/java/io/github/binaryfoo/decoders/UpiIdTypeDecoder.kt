package io.github.binaryfoo.decoders

class UpiIdTypeDecoder : PrimitiveDecoder {

    override fun decode(hexString: String): String {
        return numericToName[hexString] ?: "Unknown"
    }

    companion object {

        private val numericToName: Map<String, String> by lazy {
            csvToMap("upi-id-types.csv", 2)
        }
    }
}

