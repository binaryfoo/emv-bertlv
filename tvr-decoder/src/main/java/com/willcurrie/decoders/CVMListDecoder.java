package com.willcurrie.decoders;

import java.util.ArrayList;
import java.util.List;

import com.willcurrie.DecodedData;
import com.willcurrie.Decoder;
import com.willcurrie.tlv.ISOUtil;

public class CVMListDecoder implements Decoder {

	private static final int LENGTH_OF_AMOUNT_FIELDS_IN_CHARACTERS = 16;
	private static final int LENGTH_OF_CV_RULE = 4;

	@Override
	public List<DecodedData> decode(String input, int startIndexInBytes, DecodeSession decodeSession) {
		int x = Integer.parseInt(input.substring(0, 8), 16);
		int y = Integer.parseInt(input.substring(8, 16), 16);
		ArrayList<DecodedData> decodedData = new ArrayList<DecodedData>();
		for (int i = LENGTH_OF_AMOUNT_FIELDS_IN_CHARACTERS; i + LENGTH_OF_CV_RULE <= input.length(); i+=LENGTH_OF_CV_RULE) {
			String ruleAsHexString = input.substring(i, i + LENGTH_OF_CV_RULE);
			CVRule rule = new CVRule(ruleAsHexString);
			decodedData.add(new DecodedData(ruleAsHexString, rule.getDescription(x, y), startIndexInBytes + i/2, startIndexInBytes + (i + LENGTH_OF_CV_RULE)/2));
		}
		return decodedData;
	}

	@Override
	public int getMaxLength() {
		return 1000;
	}

	@Override
	public String validate(String bitString) {
		if (bitString == null || bitString.length() < LENGTH_OF_AMOUNT_FIELDS_IN_CHARACTERS) {
			return String.format("Value must be at least %d characters", LENGTH_OF_AMOUNT_FIELDS_IN_CHARACTERS); 
		}
		if (bitString.length() % LENGTH_OF_CV_RULE != 0) {
			return "Length must be a multiple of " + LENGTH_OF_CV_RULE;
		}
		if (!ISOUtil.isValidHexString(bitString)) {
			return "Value must contain only the characters 0-9 and A-F";
		}
		return null;
	}
	
}
