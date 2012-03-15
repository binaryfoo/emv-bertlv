package com.willcurrie.decoders;

import com.willcurrie.DecodedData;

import java.util.List;

public class ReplyAPDUDecoder {
    private TLVDecoder tlvDecoder;

    public ReplyAPDUDecoder(TLVDecoder tlvDecoder) {
        this.tlvDecoder = tlvDecoder;
    }

    public DecodedData decode(String input, int startIndexInBytes) {
        List<DecodedData> oneTlv = tlvDecoder.decode(input.substring(0, input.length() - 4), startIndexInBytes);
        DecodedData payload = oneTlv.get(0);
        int endIndex = payload.getEndIndex() + 2;
        return new DecodedData(input, "Reply", startIndexInBytes, endIndex, oneTlv);
    }
}
