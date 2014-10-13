package io.github.binaryfoo;

import io.github.binaryfoo.decoders.DecodeSession;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Decoder {

    /**
     * Turn bits into something more mind friendly.
     */
	public List<DecodedData> decode(@NotNull String input, int startIndexInBytes, @NotNull DecodeSession decodeSession);

    /**
     * Return null if ok. Otherwise an abusive/informative/educational message.
     */
	public String validate(String input);

    /**
     * In characters.
     */
	public int getMaxLength();
}
