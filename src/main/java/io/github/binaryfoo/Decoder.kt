package io.github.binaryfoo

import io.github.binaryfoo.decoders.DecodeSession

/**
 * Idea being children are shown in a tree structure.
 */
public trait Decoder {

    /**
     * Turn bits into something more mind friendly.
     */
    public fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): List<DecodedData>

    /**
     * Return null if ok. Otherwise an abusive/informative/educational message.
     */
    public fun validate(input: String?): String?

    /**
     * In characters.
     */
    public fun getMaxLength(): Int
}
