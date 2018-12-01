package io.github.binaryfoo.crypto

import io.github.binaryfoo.DecodedData

/**
 * Covers both Issuer and ICC (chip card) cases.
 */
data class RecoveredPublicKeyCertificate(
    val owner: String,
    val detail: List<DecodedData>,
    val exponentLength: String,
    val leftKeyPart: String,
    var rightKeyPart: String? = null) : PublicKeyCertificate {

  override var exponent: String? = null

  override val modulus: String
    get() = leftKeyPart + (rightKeyPart ?: "")

  override val name: String
    get() = "$owner public key"
}

