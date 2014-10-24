package io.github.binaryfoo.crypto

/**
 * Covers both Issuer and ICC (chip card) cases.
 */
public data class RecoveredPublicKeyCertificate(
        val owner: String,
        val textDump: String,
        val exponentLength: String,
        val leftKeyPart: String,
        var rightKeyPart: String? = null) : PublicKeyCertificate {

    public override var exponent: String? = null

    public override val modulus: String
        get() = leftKeyPart + (rightKeyPart ?: "")

    public override val name: String
        get() = "${owner} public key"
}

