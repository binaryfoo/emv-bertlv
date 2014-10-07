package io.github.binaryfoo.decoders;

import io.github.binaryfoo.crypto.CaPublicKey;
import io.github.binaryfoo.tlv.ISOUtil;

import java.math.BigInteger;

public class SignedDataRecoverer {

    public byte[] recover(String signed, CaPublicKey key) {
        return recover(ISOUtil.hex2byte(signed), key.getExponent(), key.getModulus());
    }

    public byte[] recover(String signed, String exponent, String modulus) {
        return recover(ISOUtil.hex2byte(signed), ISOUtil.hex2byte(exponent), ISOUtil.hex2byte(modulus));
    }

    public byte[] recover(byte[] signed, byte[] exponent, byte[] modulus) {
        BigInteger biSigned = new BigInteger(1, signed);
        BigInteger biExponent = new BigInteger(exponent);
        BigInteger bigModulus = new BigInteger(1, modulus);
        return biSigned.modPow(biExponent, bigModulus).toByteArray();
    }
}
