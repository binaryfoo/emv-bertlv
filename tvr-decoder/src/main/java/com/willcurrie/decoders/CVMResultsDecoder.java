package com.willcurrie.decoders;

import java.util.Arrays;
import java.util.List;

import com.willcurrie.DecodedData;
import com.willcurrie.Decoder;
import com.willcurrie.tlv.ISOUtil;

public class CVMResultsDecoder implements Decoder {

	private static final int FIELD_LENGTH = 6;

	@Override
	public List<DecodedData> decode(String input, int startIndexInBytes) {
		CVRule rule = new CVRule(input.substring(0, 4));
		String result = input.substring(4, 6);
		return Arrays.asList(
				new DecodedData(input.substring(0, 2), rule.getVerificationMethod().getDescription(), startIndexInBytes, startIndexInBytes + 1),
				new DecodedData(input.substring(2, 4), rule.getConditionCode().getDescription(), startIndexInBytes + 1, startIndexInBytes + 2),
				new DecodedData(result, decodeResult(result), startIndexInBytes + 2, startIndexInBytes + 3)
				);
	}

	private String decodeResult(String result) {
		return "01".equals(result) ? "Failed" :
			   "02".equals(result) ? "Sucessful" : "Unknown";
	}
	
	@Override
	public int getMaxLength() {
		return FIELD_LENGTH;
	}

	@Override
	public String validate(String bitString) {
		if (bitString == null || bitString.length() != FIELD_LENGTH) {
			return String.format("Value must be exactly %d characters", FIELD_LENGTH); 
		}
		if (!ISOUtil.isValidHexString(bitString)) {
			return "Value must contain only the characters 0-9 and A-F";
		}
		return null;
	}

}
