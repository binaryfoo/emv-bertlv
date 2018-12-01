package io.github.binaryfoo.decoders

class NullPrimitiveDecoder : PrimitiveDecoder {
  override fun decode(hexString: String): String {
    return hexString
  }
}
