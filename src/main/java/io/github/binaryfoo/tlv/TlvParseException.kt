package io.github.binaryfoo.tlv

public class TlvParseException(public val resultsSoFar: List<BerTlv>, message: String, cause: Exception) : RuntimeException(message, cause)

