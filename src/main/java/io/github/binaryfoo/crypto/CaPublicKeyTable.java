package io.github.binaryfoo.crypto;

import io.github.binaryfoo.io.ClasspathIO;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CaPublicKeyTable {

    private static final List<CaPublicKey> keys = new ArrayList<>();
    static {
        for (String line : ClasspathIO.readLines("ca-public-keys.txt")) {
            String[] fields = line.split("\\t");
            String exponent = fields[1];
            String index = fields[2];
            String aid = fields[3];
            String modulus = fields[4];
            keys.add(new CaPublicKey(aid, index, exponent, modulus));
        }
    }

    public static CaPublicKey getEntry(String aid, String index) {
        for (CaPublicKey key : keys) {
            if (key.getAid().equals(aid) && key.getIndex().equals(index)) {
                return key;
            }
        }
        return null;
    }
}
