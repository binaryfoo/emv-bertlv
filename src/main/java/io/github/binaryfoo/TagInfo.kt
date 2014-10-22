package io.github.binaryfoo

import io.github.binaryfoo.decoders.*
import kotlin.platform.platformStatic

/**
 * How a single tag should be decoded.
 */
public data class TagInfo(
        val shortName: String,
        val longName: String,
        val decoder: Decoder, // Decode the value of a BerTlv (primitive or constructed) to be included in the children field of a DecodedData.
        val primitiveDecoder: PrimitiveDecoder = PrimitiveDecoder.HEX) {

    public val fullName: String
    get() {
        return if (longName.isEmpty() || longName == shortName) shortName else shortName + " - " + longName
    }

    public fun getMaxLength(): Int {
        return decoder.getMaxLength()
    }

    /**
     * Decode the value of a PrimitiveBerTLv to be included in the decodedData field of a DecodedData.
     *
     * Idea being the decodedData is shown on the same line (same node in the tree) instead of nesting one level down.
     */
    public fun decodePrimitiveTlvValue(valueAsHexString: String): String {
        return primitiveDecoder.decode(valueAsHexString)
    }

    class object {

        platformStatic public fun treeStructured(shortName: String, longName: String, decoder: Decoder): TagInfo {
            return TagInfo(shortName, longName, decoder, PrimitiveDecoder.HEX)
        }

    }
}
