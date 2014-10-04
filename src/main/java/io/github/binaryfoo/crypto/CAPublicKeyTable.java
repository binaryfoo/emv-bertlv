package io.github.binaryfoo.crypto;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CaPublicKeyTable {

    private static final List<CaPublicKey> keys = new ArrayList<>();
    static {
        try (InputStream in = CaPublicKeyTable.class.getClassLoader().getResourceAsStream("ca-public-keys.txt")) {
            for (String line : IOUtils.readLines(in)) {
                String[] fields = line.split("\\t");
                String exponent = fields[1];
                String index = fields[2];
                String aid = fields[3];
                String modulus = fields[4];
                keys.add(new CaPublicKey(aid, index, exponent, modulus));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load codes", e);
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
