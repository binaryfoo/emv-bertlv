package io.github.binaryfoo.tlv

class TlvParseException(val resultsSoFar: List<BerTlv>, message: String, cause: Exception) : RuntimeException(message, cause)

