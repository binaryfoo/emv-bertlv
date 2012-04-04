package com.willcurrie.decoders;

import com.willcurrie.DecodedData;
import com.willcurrie.Decoder;

import java.util.Collections;
import java.util.List;

public class NullDecoder implements Decoder {
    @Override
    public List<DecodedData> decode(String input, int startIndexInBytes, DecodeSession decodeSession) {
        return Collections.emptyList();
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
