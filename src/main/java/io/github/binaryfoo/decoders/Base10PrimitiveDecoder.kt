package io.github.binaryfoo.decoders

public class Base10PrimitiveDecoder : PrimitiveDecoder {
    override fun decode(hexString: String): String {
        return Integer.parseInt(hexString, 16).toString()
    }
}
