package io.github.binaryfoo.decoders

import io.github.binaryfoo.io.ClasspathIO

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
            val map = HashMap<String, String>()
            map.putAll(ClasspathIO.readLines("numeric-currency-list.csv").map { line ->
                val numeric = line.substring(0, 3)
                val alpha = line.substring(5)
                numeric to alpha
            })
            map
        }
    }

}
