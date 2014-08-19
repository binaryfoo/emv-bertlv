package com.willcurrie.decoders;

import com.willcurrie.DecodedData;
import com.willcurrie.Decoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrencyCodeDecoder implements Decoder {

    private static Map<String, String> numericToAlpha;

    @Override
    public List<DecodedData> decode(String input, int startIndexInBytes, DecodeSession decodeSession) {
        String alpha = getNumericToAlpha().get(input.substring(1));
        return Arrays.asList(new DecodedData(input, alpha == null ? "Unknown" : alpha, startIndexInBytes, startIndexInBytes + 2));
    }

    @Override
    public String validate(String input) {
        return null;
    }

    @Override
    public int getMaxLength() {
        return 0;
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
