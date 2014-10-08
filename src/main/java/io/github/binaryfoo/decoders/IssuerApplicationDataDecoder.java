package io.github.binaryfoo.decoders;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.Decoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IssuerApplicationDataDecoder implements Decoder {
    @Override
    public List<DecodedData> decode(String input, int startIndexInBytes, DecodeSession decodeSession) {
        try {
            return decodeVisaIad(input, startIndexInBytes, decodeSession);
        } catch (Exception ignored) {
        }
        return Collections.emptyList();
    }

    /*
     * From Visa Contactless Payment Specification v2.1
     */
    private List<DecodedData> decodeVisaIad(String input, int startIndexInBytes, DecodeSession decodeSession) {
        List<DecodedData> decoded = new ArrayList<DecodedData>();
        int length = Integer.parseInt(input.substring(0, 2), 16);
        String dki = input.substring(2, 4);
        decoded.add(new DecodedData("Derivation key index", dki, startIndexInBytes + 1, startIndexInBytes + 2));
        String cvn = input.substring(4, 6);
        decoded.add(new DecodedData("Cryptogram version number", cvn, startIndexInBytes + 2, startIndexInBytes + 3));
        String trueCvr = input.substring(6, 2 + length * 2);
        String decodableCvr = input.substring(6, 6 + 8); // until more specs about CVR
        decoded.add(new DecodedData("Card verification results", trueCvr, startIndexInBytes + 3, startIndexInBytes + (6 + Math.min(8, length * 2)) /2, new VisaCardVerificationResultsDecoder().decode(decodableCvr, startIndexInBytes + 3, decodeSession)));
        if (input.length() > 2 + length * 2) {
            int iddLength = Integer.parseInt(input.substring(14, 16), 16);
            int endIndex = 16 + iddLength * 2;
            if ((iddLength > 0) && (endIndex <= input.length())) {
                String idd = input.substring(16, endIndex);
                decoded.add(new DecodedData("Issuer discretionary data", idd, startIndexInBytes + 8, startIndexInBytes + 1 + iddLength));
            }
        }
        return decoded;
    }

    @Override
    public String validate(String input) {
        return null;
    }

    @Override
    public int getMaxLength() {
        return 0;
    }
}
