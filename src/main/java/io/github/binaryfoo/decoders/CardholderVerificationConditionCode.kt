package io.github.binaryfoo.decoders

/**
 * When a given method for verifying a cardholder should be applied.
 */
enum class CardholderVerificationConditionCode(val code: Int, val description: String) {
  Always(0, "Always"),
  UnattendedCash(1, "If unattended cash"),
  NotStuff(2, "If not (unattended cash, manual cash, purchase + cash)"),
  TerminalSupports(3, "If terminal supports CVM"),
  ManualCash(4, "If manual cash"),
  PurchasePlusCash(5, "If purchase + cash"),
  TxLessThanX(6, "If transaction in application currency and < X"),
  TxMoreThanX(7, "If transaction in application currency and >= X"),
  TxLessThanY(8, "If transaction in application currency and < Y"),
  TxMoreThanY(9, "If transaction in application currency and >= Y");

  companion object {
    fun fromCode(code: Int): CardholderVerificationConditionCode? = values().firstOrNull { it.code == code }
  }
}
