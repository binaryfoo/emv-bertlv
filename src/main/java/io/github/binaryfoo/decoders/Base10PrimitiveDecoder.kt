package io.github.binaryfoo.decoders

import kotlin.text.isEmpty

public class Base10PrimitiveDecoder : PrimitiveDecoder {
    override fun decode(hexString: String): String {
        return if (hexString.isEmpty()) "" else Integer.parseInt(hexString, 16).toString()
    }
}
