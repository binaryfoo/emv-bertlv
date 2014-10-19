package io.github.binaryfoo.decoders;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.Decoder;
import io.github.binaryfoo.tlv.ISOUtil;
import io.github.binaryfoo.tlv.Tag;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DataObjectListDecoder implements Decoder {
    @Override
    public List<DecodedData> decode(@NotNull String input, int startIndexInBytes, @NotNull DecodeSession decodeSession) {
        List<DecodedData> children = new ArrayList<DecodedData>();
        ByteBuffer buffer = ByteBuffer.wrap(ISOUtil.hex2byte(input));
        int offset = startIndexInBytes;
        while (buffer.hasRemaining()) {
            Tag tag = Tag.parse(buffer);
            byte b = buffer.get();
            int newOffset = offset + tag.getBytes().length + 1;
            children.add(new DecodedData("", tag.toString(decodeSession.getTagMetaData()) + " " + b + " bytes", offset, newOffset));
            offset = newOffset;
        }
        return children;
    }

    @Override
    public String validate(String input) {
        if (!input.matches("^[0-9A-Za-z]$")) {
            return "Only A-Z and 0-9 are valid";
        }
        return null;
    }

    @Override
    public int getMaxLength() {
        return Integer.MAX_VALUE;
    }
}
