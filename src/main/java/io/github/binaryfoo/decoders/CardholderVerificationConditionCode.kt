package io.github.binaryfoo.decoders

/**
 * When a given method for verifying a cardholder should be applied.
 */
public enum class CardholderVerificationConditionCode(public val code: Int, public val description: String) {
    Always : CardholderVerificationConditionCode(0, "Always")
    UnattendedCash : CardholderVerificationConditionCode(1, "If unattended cash")
    NotStuff : CardholderVerificationConditionCode(2, "If not (unattended cash, manual cash, purchase + cash)")
    TerminalSupports : CardholderVerificationConditionCode(3, "If terminal supports CVM")
    ManualCash : CardholderVerificationConditionCode(4, "If manual cash")
    PurchasePlusCash : CardholderVerificationConditionCode(5, "If purchase + cash")
    TxLessThanX : CardholderVerificationConditionCode(6, "If transaction in application currency and < X")
    TxMoreThanX : CardholderVerificationConditionCode(7, "If transaction in application currency and >= X")
    TxLessThanY : CardholderVerificationConditionCode(8, "If transaction in application currency and < Y")
    TxMoreThanY : CardholderVerificationConditionCode(9, "If transaction in application currency and >= Y")

    class object {
        public fun fromCode(code: Int): CardholderVerificationConditionCode? = values().firstOrNull { it.code == code }
    }
}
