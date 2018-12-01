package io.github.binaryfoo.decoders

/**
 * A way of verifying a cardholder ... is allowed to hold the card they're presenting.
 */
enum class CardholderVerificationMethod(val code: Int, val description: String) {
  Fail(0, "Fail"),
  PlainPinByIcc(1, "Plain PIN by ICC"),
  EncryptedPinOnline(2, "Encrypted PIN online"),
  PlainPinByIccPlusSignature(3, "Plain PIN by ICC + signature"),
  EncryptedPinByIcc(4, "Encrypted PIN by ICC"),
  EncryptedPinByIccPlusSignature(5, "Encrypted PIN by ICC + signature"),
  Signature(30, "Signature"),
  NoCvmRequired(31, "No CVM required");

  companion object {
    fun fromCode(code: Int): CardholderVerificationMethod? = values().firstOrNull() { it.code == code }
  }
}
