package io.github.binaryfoo;

import io.github.binaryfoo.decoders.DecodeSession;

import java.util.List;

public interface Decoder {

    /**
     * Turn bits into something more mind friendly.
     */
	public List<DecodedData> decode(String input, int startIndexInBytes, DecodeSession decodeSession);

    /**
     * Return null if ok. Otherwise an abusive/informative/educational message.
     */
	public String validate(String input);

    /**
     * In characters.
     */
	public int getMaxLength();
}
