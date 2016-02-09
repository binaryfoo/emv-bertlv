package io.github.binaryfoo.decoders

import kotlin.collections.firstOrNull

enum class CryptogramType constructor(val fullName: String, val meaning: String, val hex: String) {
    AAC("Application Authentication Cryptogram", "Declined", "00"),
    ARQC("Authorisation Request Cryptogram", "Go ask the issuer", "80"),
    TC("Transaction Certificate", "Approved", "40");

    companion object {
        fun fromHex(hex: String): CryptogramType? = values().firstOrNull { it.hex == hex}
    }

    override fun toString(): String = "$name ($fullName - $meaning)"
}
