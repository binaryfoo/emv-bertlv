package io.github.binaryfoo.decoders

public class NullPrimitiveDecoder : PrimitiveDecoder {
    override fun decode(hexString: String): String {
        return hexString
    }
}
