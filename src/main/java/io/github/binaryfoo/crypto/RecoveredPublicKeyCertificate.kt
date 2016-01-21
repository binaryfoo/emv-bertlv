package io.github.binaryfoo.crypto

import io.github.binaryfoo.DecodedData

/**
 * Covers both Issuer and ICC (chip card) cases.
 */
public data class RecoveredPublicKeyCertificate(
        val owner: String,
        val detail: List<DecodedData>,
        val exponentLength: String,
        val leftKeyPart: String,
        var rightKeyPart: String? = null) : PublicKeyCertificate {

    public override var exponent: String? = null

    public override val modulus: String
        get() = leftKeyPart + (rightKeyPart ?: "")

    public override val name: String
        get() = "$owner public key"
}

