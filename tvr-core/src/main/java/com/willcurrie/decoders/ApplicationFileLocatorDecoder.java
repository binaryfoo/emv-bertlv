package com.willcurrie.decoders;

import com.willcurrie.DecodedData;
import com.willcurrie.Decoder;

import java.util.ArrayList;
import java.util.List;

/**
 * EMV Book 3 (v4.3) 10.2 Read Application Data
 */
public class ApplicationFileLocatorDecoder implements Decoder {

    @Override
    public List<DecodedData> decode(String input, int startIndexInBytes, DecodeSession decodeSession) {
        ArrayList<DecodedData> decoded = new ArrayList<DecodedData>();
        for (int offset = 0; offset < input.length(); offset += 8) {
            String element = input.substring(offset, offset + 8);
            int sfi = Integer.parseInt(element.substring(0, 2), 16) >> 3;
            int firstRecord = Integer.parseInt(element.substring(2, 4));
            int lastRecord = Integer.parseInt(element.substring(4, 6));
            String range =  firstRecord + (lastRecord == firstRecord ? "" : "-" + lastRecord);
            decoded.add(new DecodedData("", "SFI " + sfi + " number " + range, startIndexInBytes + (offset / 2), startIndexInBytes + (offset / 2) + 4));
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
