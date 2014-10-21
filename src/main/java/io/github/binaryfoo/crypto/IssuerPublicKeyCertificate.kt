package io.github.binaryfoo.crypto

public data class IssuerPublicKeyCertificate(
        val textDump: String,
        val exponentLength: String,
        val leftKeyPart: String,
        var rightKeyPart: String? = null) {

    public val fullKey: String
        get() = leftKeyPart + (rightKeyPart ?: "")
}

