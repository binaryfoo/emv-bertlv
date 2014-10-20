package io.github.binaryfoo.crypto

import io.github.binaryfoo.io.ClasspathIO
import org.apache.commons.io.IOUtils

import java.io.IOException
import java.io.InputStream
import java.util.ArrayList
import kotlin.platform.platformStatic

public class CaPublicKeyTable {
    class object {

        private val keys = ClasspathIO.readLines("ca-public-keys.txt").map { line ->
            val fields = line.split("\\t")
            val exponent = fields[1]
            val index = fields[2]
            val aid = fields[3]
            val modulus = fields[4]
            CaPublicKey(aid, index, exponent, modulus)
        }

        platformStatic public fun getEntry(aid: String, index: String): CaPublicKey? = keys.firstOrNull { it.aid == aid && it.index == index }
    }
}
