package io.github.binaryfoo.crypto;

import io.github.binaryfoo.tlv.ISOUtil;

public class CaPublicKey {
    private final String aid;
    private final String index;
    private final byte[] exponent;
    private final byte[] modulus;

    public CaPublicKey(String aid, String index, String exponent, String modulus) {
        this.aid = aid;
        this.index = index;
        this.exponent = ISOUtil.hex2byte(exponent);
        this.modulus = ISOUtil.hex2byte(modulus);
    }

    public String getAid() {
        return aid;
    }

    public String getIndex() {
        return index;
    }

    public byte[] getExponent() {
        return exponent;
    }

    public byte[] getModulus() {
        return modulus;
    }
}
