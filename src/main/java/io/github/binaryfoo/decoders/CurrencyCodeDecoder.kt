package io.github.binaryfoo.decoders

import io.github.binaryfoo.res.ClasspathIO

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.HashMap
import kotlin.properties.Delegates

public class CurrencyCodeDecoder : PrimitiveDecoder {

    override fun decode(hexString: String): String {
        return numericToAlpha[hexString.substring(1)] ?: "Unknown"
    }

    class object {

        private val numericToAlpha: Map<String, String> by Delegates.blockingLazy {
            csvToMap("numeric-currency-list.csv", 3)
        }
    }

}
