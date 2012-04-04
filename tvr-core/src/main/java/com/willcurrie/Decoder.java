package com.willcurrie;

import com.willcurrie.decoders.DecodeSession;

import java.util.List;

public interface Decoder {

	public List<DecodedData> decode(String input, int startIndexInBytes, DecodeSession decodeSession);
	
	public String validate(String input);
	
	public int getMaxLength();
}
