package io.github.binaryfoo.decoders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public abstract class CodeToAlphaDecoder implements PrimitiveDecoder {

    private final int codeLength;
    private final String source;

    /**
     * 
     * @param source CSV file with source data
     * @param codeLength length of first column in source file (followed by comma, space and alpha-description of the code
     */
    public CodeToAlphaDecoder(String source, int codeLength) {
        this.codeLength = codeLength;
        this.source = source;
    }

    private Map<String, String> numericToAlpha; // Is it OK not to have this static?

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
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(CodeToAlphaDecoder.class.getClassLoader().getResourceAsStream(source)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String numeric = line.substring(0, codeLength);
                String alpha = line.substring(codeLength + 2);
                codes.put(numeric, alpha);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return codes;
    }

}
