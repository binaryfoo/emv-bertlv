package com.willcurrie;

import java.util.List;

public interface Decoder {

	public List<DecodedData> decode(String input, int startIndex);
	
	public String validate(String input);
	
	public int getMaxLength();
}
