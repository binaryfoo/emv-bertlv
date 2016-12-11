package io.github.binaryfoo

import io.github.binaryfoo.decoders.*
import kotlin.collections.mapOf
import kotlin.text.isEmpty

/**
 * How a single tag should be decoded.
 */
data class TagInfo(
        val shortName: String,
        val longName: String,
        val decoder: Decoder, // Decode the value of a BerTlv (primitive or constructed) to be included in the children field of a DecodedData.
        val primitiveDecoder: PrimitiveDecoder = PrimitiveDecoder.HEX,
        val shortBackground: String? = null,
        val longBackground: String? = null) {

    val backgroundReading: Map<String, String?>?
    get() {
        if (shortBackground != null || longBackground != null) {
            return mapOf("short" to shortBackground, "long" to longBackground)
        } else {
            return null
        }
    }

    val fullName: String
    get() {
        return if (longName.isEmpty() || longName == shortName) shortName else shortName + " - " + longName
    }

    fun getMaxLength(): Int {
        return decoder.getMaxLength()
    }

    /**
     * Decode the value of a PrimitiveBerTLv to be included in the decodedData field of a DecodedData.
     *
     * Idea being the decodedData is shown on the same line (same node in the tree) instead of nesting one level down.
     */
    fun decodePrimitiveTlvValue(valueAsHexString: String): String {
        return if (valueAsHexString.isNullOrBlank()) "" else primitiveDecoder.decode(valueAsHexString)
    }

    companion object {

        @JvmStatic fun treeStructured(shortName: String, longName: String, decoder: Decoder, shortBackground: String? = null, longBackground: String? = null): TagInfo {
            return TagInfo(shortName, longName, decoder, PrimitiveDecoder.HEX, shortBackground, longBackground)
        }

    }
}
