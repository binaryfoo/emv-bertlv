package io.github.binaryfoo.decoders

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.Decoder

import java.util.Collections
import kotlin.collections.listOf

public class NullDecoder : Decoder {
    override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): List<DecodedData> {
        return listOf()
    }

    override fun validate(input: String?): String? {
        return null
    }

    override fun getMaxLength(): Int {
        return 0
    }
}
