package com.willcurrie.decoders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class CurrencyCodeDecoder implements PrimitiveDecoder {

    private static Map<String, String> numericToAlpha;


    @Override
    public String decode(String hexString) {
        String alpha = getNumericToAlpha().get(hexString.substring(1));
        return alpha == null ? "Unknown" : alpha;
    }

    private synchronized Map<String, String> getNumericToAlpha() {
        if (numericToAlpha == null) {
            numericToAlpha = load();
        }
        return numericToAlpha;
    }

    private Map<String, String> load() {
        Map<String, String> codes = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(CurrencyCodeDecoder.class.getClassLoader().getResourceAsStream("numeric-currency-list.csv")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String numeric = line.substring(0, 3);
                String alpha = line.substring(5);
                codes.put(numeric, alpha);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return codes;
    }

}
