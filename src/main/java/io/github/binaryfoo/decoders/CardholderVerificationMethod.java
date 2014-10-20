package io.github.binaryfoo.decoders;

/**
 * A way of verifying a cardholder ... is allowed to hold the card they're presenting.
 */
public enum CardholderVerificationMethod {
    Fail(0x00, "Fail"),
    PlainPinByIcc(0x01, "Plain PIN by ICC"),
    EncryptedPinOnline(0x02, "Encrypted PIN online"),
    PlainPinByIccPlusSignature(0x03, "Plain PIN by ICC + signature"),
    EncryptedPinByIcc(0x04, "Encrypted PIN by ICC"),
    EncryptedPinByIccPlusSignature(0x05, "Encrypted PIN by ICC + signature"),
    Signature(0x1E, "Signature"),
    NoCvmRequired(0x1F, "No CVM required");

    private final int code;
    private final String description;

    CardholderVerificationMethod(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static CardholderVerificationMethod fromCode(int code) {
        for (CardholderVerificationMethod verificationMethod : CardholderVerificationMethod.values()) {
            if (verificationMethod.code == code) {
                return verificationMethod;
            }
        }
        return null;
    }
}
