package com.willcurrie.decoders;

import java.math.BigInteger;

public class SignedDataRecoverer {

    public byte[] recover(byte[] signed, byte[] exponent, byte[] modulus) {
        BigInteger biSigned = new BigInteger(1, signed);
        BigInteger biExponent = new BigInteger(exponent);
        BigInteger bigModulus = new BigInteger(1, modulus);
        return biSigned.modPow(biExponent, bigModulus).toByteArray();
    }
}
