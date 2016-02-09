package io.github.binaryfoo.decoders

import kotlin.text.substring

class CountryCodeDecoder : PrimitiveDecoder {

    override fun decode(hexString: String): String {
        return numericToAlpha[hexString.substring(1)] ?: "Unknown"
    }

    companion object {

        private val numericToAlpha: Map<String, String> by lazy {
            csvToMap("numeric-country-list.csv", 3)
        }
    }
}
