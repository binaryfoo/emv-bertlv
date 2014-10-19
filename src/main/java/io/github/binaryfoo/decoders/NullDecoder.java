package io.github.binaryfoo.decoders;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.Decoder;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class NullDecoder implements Decoder {
    @Override
    public List<DecodedData> decode(@NotNull String input, int startIndexInBytes, @NotNull DecodeSession decodeSession) {
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
