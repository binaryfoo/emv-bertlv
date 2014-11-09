package io.github.binaryfoo.decoders

public enum class CryptogramType(public val name: String, public val meaning: String, public val hex: String) {
    AAC : CryptogramType("Application Authentication Cryptogram", "Declined", "00")
    ARQC : CryptogramType("Authorisation Request Cryptogram", "Go ask the issuer", "80")
    TC : CryptogramType("Transaction Certificate", "Approved", "40")

    class object {
        public fun fromHex(hex: String): CryptogramType? = values().firstOrNull { it.hex == hex}
    }

    override fun toString(): String = "${name()} ($name - $meaning)"
}
