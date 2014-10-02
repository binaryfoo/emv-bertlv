package io.github.binaryfoo.decoders;

public enum CryptogramType {
    AAC("Application Authentication Cryptogram", "Declined", "00"),
    ARQC("Authorisation Request Cryptogram", "Go ask the issuer", "80"),
    TC("Transaction Certificate", "Approved", "40");

    private final String name;
    private final String meaning;
    private final String hex;

    CryptogramType(String name, String meaning, String hex) {
        this.name = name;
        this.meaning = meaning;
        this.hex = hex;
    }

    public static CryptogramType fromHex(String hex) {
        for (CryptogramType c : values()) {
            if (c.hex.equals(hex)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name() + " (" + name + " - " + meaning + ")";
    }
}
