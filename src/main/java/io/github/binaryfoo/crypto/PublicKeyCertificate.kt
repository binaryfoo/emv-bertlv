package io.github.binaryfoo.crypto

/**
 * Crypto public key. Covers the chain from Scheme (CA) to Issuer (Bank) and then ICC (chip).
 */
public trait PublicKeyCertificate {
    public val exponent: String?
    public val modulus: String
    public val name: String
    public val modulusLength: Int
        get() = modulus.size / 2
}

