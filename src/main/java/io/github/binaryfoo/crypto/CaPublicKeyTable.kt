package io.github.binaryfoo.crypto

import io.github.binaryfoo.res.ClasspathIO
import org.apache.commons.io.IOUtils

import java.io.IOException
import java.io.InputStream
import java.util.ArrayList
import kotlin.platform.platformStatic

public class CaPublicKeyTable {
    class object {

        public val keys: List<CaPublicKey> = ClasspathIO.readLines("ca-public-keys.txt").map { line ->
            try {
                val fields = line.split("\\t")
                val exponent = fields[1]
                val index = fields[2]
                val rid = fields[3]
                val modulus = fields[4]
                CaPublicKey(rid, index, exponent, modulus)
            } catch(e: Exception) {
                throw RuntimeException("Failed reading CA public key: ${line}", e)
            }
        }

        platformStatic public fun getEntry(aid: String, index: String): CaPublicKey? = keys.firstOrNull { it.rid == aid && it.index == index }
    }
}
