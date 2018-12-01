package io.github.binaryfoo.crypto

/**
 * One of the trust anchors. From the scheme: Visa, Mastercard, JCB, Amex, ...
 */
data class CaPublicKey(
    val rid: String,
    val index: String,
    override val exponent: String,
    override val modulus: String) : PublicKeyCertificate {
  override val name: String = "CA public key ($rid,$index)"
}
