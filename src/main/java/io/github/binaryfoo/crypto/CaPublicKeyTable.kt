package io.github.binaryfoo.crypto

import io.github.binaryfoo.res.ClasspathIO
import kotlin.collections.firstOrNull
import kotlin.collections.map
import kotlin.text.split

/**
 * Table of trust anchors.
 *
 * From https://www.eftlab.co.uk/index.php/site-map/knowledge-base/243-ca-public-keys
 */
public class CaPublicKeyTable {
    companion object {

        public val keys: List<CaPublicKey> = ClasspathIO.readLines("ca-public-keys.txt").map { line ->
            try {
                val fields = line.split(Regex("\\t"))
                val exponent = fields[1]
                val index = fields[2]
                val rid = fields[3]
                val modulus = fields[4]
                CaPublicKey(rid, index, exponent, modulus)
            } catch(e: Exception) {
                throw RuntimeException("Failed reading CA public key: $line", e)
            }
        }

        @JvmStatic public fun getEntry(aid: String, index: String): CaPublicKey? = keys.firstOrNull { it.rid == aid && it.index == index }
    }
}
