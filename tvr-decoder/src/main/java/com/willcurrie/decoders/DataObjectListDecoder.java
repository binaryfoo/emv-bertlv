package com.willcurrie.decoders;

import com.willcurrie.DecodedData;
import com.willcurrie.Decoder;
import com.willcurrie.tlv.ISOUtil;
import com.willcurrie.tlv.Tag;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataObjectListDecoder implements Decoder {
    @Override
    public List<DecodedData> decode(String input, int startIndexInBytes) {
        List<DecodedData> children = new ArrayList<DecodedData>();
        ByteBuffer buffer = ByteBuffer.wrap(ISOUtil.hex2byte(input));
        int offset = startIndexInBytes;
        while (buffer.hasRemaining()) {
            Tag tag = Tag.parse(buffer);
            byte b = buffer.get();
            int newOffset = offset + tag.getBytes().length + 1;
            children.add(new DecodedData("", tag.toString() + " " + b + " bytes", offset, newOffset));
            offset = newOffset;
        }
        return Arrays.asList(new DecodedData(input, "data object list", startIndexInBytes, input.length()/2, children));
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
