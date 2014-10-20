package io.github.binaryfoo.decoders;

/**
 * When a given method for verifying a cardholder should be applied.
 */
public enum CardholderVerificationConditionCode {
    Always(0x00, "Always"),
    UnattendedCash(0x01, "If unattended cash"),
    NotStuff(0x02, "If not (unattended cash, manual cash, purchase + cash)"),
    TerminalSupports(0x03, "If terminal supports CVM"),
    ManualCash(0x04, "If manual cash"),
    PurchasePlusCash(0x05, "If purchase + cash"),
    TxLessThanX(0x06, "If transaction in application currency and < X"),
    TxMoreThanX(0x07, "If transaction in application currency and >= X"),
    TxLessThanY(0x08, "If transaction in application currency and < Y"),
    TxMoreThanY(0x09, "If transaction in application currency and >= Y");

    private final int code;
    private final String description;

    CardholderVerificationConditionCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static CardholderVerificationConditionCode fromCode(int code) {
        for (CardholderVerificationConditionCode conditionCode : CardholderVerificationConditionCode.values()) {
            if (conditionCode.code == code) {
                return conditionCode;
            }
        }
        return null;
    }
}
