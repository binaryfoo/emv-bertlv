package io.github.binaryfoo.decoders;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.Decoder;

import java.util.ArrayList;
import java.util.List;

// bit masked with 9F35 in the GPO R-APDU
public class AmexTerminalCapabilitiesDecoder implements Decoder {

    @Override
    public List<DecodedData> decode(String input, int startIndexInBytes, DecodeSession decodeSession) {
        List<DecodedData> decodedData = new ArrayList<DecodedData>();
        int field = Integer.parseInt(input, 16);
        if ((field & 0xC000) == 0x0) {
            decodedData.add(new DecodedData("", "Expresspay 1.0", startIndexInBytes, startIndexInBytes + 2));
        }
        if ((field & 0x4000) == 0x4000) {
            decodedData.add(new DecodedData("", "Expresspay 2.0 MSD", startIndexInBytes, startIndexInBytes + 2));
        }
        if ((field & 0x8000) == 0x8000) {
            decodedData.add(new DecodedData("", "Expresspay 2.0 EMV & MSD", startIndexInBytes, startIndexInBytes + 2));
        }
        if ((field & 0xC000) == 0xC000) {
            decodedData.add(new DecodedData("", "Expresspay Mobile - EMV", startIndexInBytes, startIndexInBytes + 2));
        }
        if ((field & 0x0800) == 0x0) {
            decodedData.add(new DecodedData("", "CVM Not Required", startIndexInBytes, startIndexInBytes + 2));
        } else {
            decodedData.add(new DecodedData("", "CVM Required", startIndexInBytes, startIndexInBytes + 2));
        }
        return decodedData;
    }

    @Override
    public String validate(String input) {
        return null;
    }

    @Override
    public int getMaxLength() {
        return 4;
    }
}
