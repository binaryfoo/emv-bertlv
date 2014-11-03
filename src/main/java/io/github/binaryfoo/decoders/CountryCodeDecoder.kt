package io.github.binaryfoo.decoders

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.HashMap
import kotlin.properties.Delegates
import io.github.binaryfoo.res.ClasspathIO

public class CountryCodeDecoder : PrimitiveDecoder {

    override fun decode(hexString: String): String {
        return numericToAlpha[hexString.substring(1)] ?: "Unknown"
    }

    class object {

        private val numericToAlpha: Map<String, String> by Delegates.blockingLazy {
            csvToMap("numeric-country-list.csv", 3)
        }
    }
}
