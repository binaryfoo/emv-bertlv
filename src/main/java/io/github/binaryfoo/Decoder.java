package io.github.binaryfoo;

import io.github.binaryfoo.decoders.DecodeSession;

import java.util.List;

public interface Decoder {

	public List<DecodedData> decode(String input, int startIndexInBytes, DecodeSession decodeSession);
	
	public String validate(String input);

    /**
     * In characters.
     */
	public int getMaxLength();
}
