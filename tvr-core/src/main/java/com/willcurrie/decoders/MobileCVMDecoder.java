package com.willcurrie.decoders;

import com.willcurrie.DecodedData;
import com.willcurrie.Decoder;

import java.util.ArrayList;
import java.util.List;

// Table 12: Mobile CVM Results (Tag 9F71) Expresspay 3.0¡
public class MobileCVMDecoder implements Decoder {

    @Override
    public List<DecodedData> decode(String input, int startIndexInBytes, DecodeSession decodeSession) {
        List<DecodedData> decodedData = new ArrayList<DecodedData>();
        int field = Integer.parseInt(input, 16);
        if ((field & 0x010000) == 0x010000) {
            decodedData.add(new DecodedData("", "Mobile CVM Performed", startIndexInBytes, startIndexInBytes + 1));
        }
        if ((field & 0x3F0000) == 0x3F0000) {
            decodedData.add(new DecodedData("", "No CVM Performed", startIndexInBytes, startIndexInBytes + 1));
        }
        if ((field & 0x000300) == 0x0) {
            decodedData.add(new DecodedData("", "Mobile CVM Not Required", startIndexInBytes + 1, startIndexInBytes + 2));
        } else {
            decodedData.add(new DecodedData("", "Terminal Required CVM", startIndexInBytes + 1, startIndexInBytes + 2));
        }
        if ((field & 0x000001) == 0x000001) {
            decodedData.add(new DecodedData("", "Mobile CVM Failed", startIndexInBytes + 2, startIndexInBytes + 3));
        }
        if ((field & 0x000002) == 0x000002) {
            decodedData.add(new DecodedData("", "Mobile CVM Successful", startIndexInBytes + 2, startIndexInBytes + 3));
        }
        if ((field & 0x000003) == 0x000003) {
            decodedData.add(new DecodedData("", "Mobile CVM Blocked", startIndexInBytes + 2, startIndexInBytes + 3));
        }
        return decodedData;
    }

    @Override
    public String validate(String input) {
        return null;
    }

    @Override
    public int getMaxLength() {
        return 6;
    }
}
