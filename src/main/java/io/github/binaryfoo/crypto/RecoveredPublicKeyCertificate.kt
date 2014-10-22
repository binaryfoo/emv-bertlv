package io.github.binaryfoo.crypto

/**
 * Covers by Issuer and ICC (chip card).
 */
public data class RecoveredPublicKeyCertificate(
        val textDump: String,
        val exponentLength: String,
        val leftKeyPart: String,
        var rightKeyPart: String? = null) {

    public val fullKey: String
        get() = leftKeyPart + (rightKeyPart ?: "")
}

