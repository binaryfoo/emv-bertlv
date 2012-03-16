package com.willcurrie.decoders;

import com.willcurrie.DecodedData;

import java.util.Collections;
import java.util.List;

public class ReplyAPDUDecoder {
    private TLVDecoder tlvDecoder;

    public ReplyAPDUDecoder(TLVDecoder tlvDecoder) {
        this.tlvDecoder = tlvDecoder;
    }

    public DecodedData decode(String input, int startIndexInBytes) {
        int statusBytesStart = input.length() - 4;
        int endIndex;
        List<DecodedData> children;
        if (input.length() == 4) {
            children = Collections.emptyList();
            endIndex = startIndexInBytes + 2;
        } else {
            children = tlvDecoder.decode(input.substring(0, statusBytesStart), startIndexInBytes);
            DecodedData payload = children.get(0);
            endIndex = payload.getEndIndex() + 2;
        }
        return new DecodedData("Reply", input.substring(statusBytesStart), startIndexInBytes, endIndex, children);
    }
}
