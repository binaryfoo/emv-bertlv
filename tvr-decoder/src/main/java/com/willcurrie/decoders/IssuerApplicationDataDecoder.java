package com.willcurrie.decoders;

import com.willcurrie.DecodedData;
import com.willcurrie.Decoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IssuerApplicationDataDecoder implements Decoder {
    @Override
    public List<DecodedData> decode(String input, int startIndexInBytes, DecodeSession decodeSession) {
        try {
            return decodeVisaIad(input, startIndexInBytes);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private List<DecodedData> decodeVisaIad(String input, int startIndexInBytes) {
        List<DecodedData> decoded = new ArrayList<DecodedData>();
        String dki = input.substring(2, 4);
        decoded.add(new DecodedData("Derivation key index", dki, startIndexInBytes + 1, startIndexInBytes + 2));
        String cvn = input.substring(4, 6);
        decoded.add(new DecodedData("Cryptogram version number", cvn, startIndexInBytes + 2, startIndexInBytes + 3));
        String cvr = input.substring(6, 14);
        decoded.add(new DecodedData("Card verification results", cvr, startIndexInBytes + 3, startIndexInBytes + 7));
        if (input.length() > 14) {
            int iddLength = Integer.parseInt(input.substring(14, 16), 16);
            if (iddLength > 0) {
                String idd = input.substring(16, 16 + iddLength * 2);
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
