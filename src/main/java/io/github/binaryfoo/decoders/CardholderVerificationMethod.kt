package io.github.binaryfoo.decoders

/**
 * A way of verifying a cardholder ... is allowed to hold the card they're presenting.
 */
public enum class CardholderVerificationMethod(public val code: Int, public val description: String) {
    Fail : CardholderVerificationMethod(0, "Fail")
    PlainPinByIcc : CardholderVerificationMethod(1, "Plain PIN by ICC")
    EncryptedPinOnline : CardholderVerificationMethod(2, "Encrypted PIN online")
    PlainPinByIccPlusSignature : CardholderVerificationMethod(3, "Plain PIN by ICC + signature")
    EncryptedPinByIcc : CardholderVerificationMethod(4, "Encrypted PIN by ICC")
    EncryptedPinByIccPlusSignature : CardholderVerificationMethod(5, "Encrypted PIN by ICC + signature")
    Signature : CardholderVerificationMethod(30, "Signature")
    NoCvmRequired : CardholderVerificationMethod(31, "No CVM required")

    class object {
        public fun fromCode(code: Int): CardholderVerificationMethod? = values().firstOrNull() { it.code == code }
    }
}
