package com.willcurrie.decoders;

import java.util.ArrayList;
import java.util.List;

import com.willcurrie.DecodedData;
import com.willcurrie.Decoder;
import com.willcurrie.tlv.ISOUtil;

public class FixedLengthDecoder implements Decoder {

	private final int lengthInCharacters;
	private final List<BitMapping> bits;

	public FixedLengthDecoder(int lengthInCharacters, String... bitAndDescriptions) {
		this.lengthInCharacters = lengthInCharacters;
		bits = parseBitsAndDescriptions(bitAndDescriptions);
	}
	
	private List<BitMapping> parseBitsAndDescriptions(String[] bitsAndDescriptions) {
		assert bitsAndDescriptions.length % 2 == 0;
		ArrayList<BitMapping> bits = new ArrayList<BitMapping>(bitsAndDescriptions.length / 2);
		for (int i = 0; i < bitsAndDescriptions.length; i+=2) {
			String bitAsHexString = bitsAndDescriptions[i];
			String description = bitsAndDescriptions[i + 1];
			bits.add(new BitMapping(bitAsHexString, description));
		}
		return bits;
	}

	public String validate(String bitString) {
		if (bitString == null || bitString.length() != lengthInCharacters) {
			return String.format("Value must be exactly %d characters", lengthInCharacters); 
		}
		if (!ISOUtil.isValidHexString(bitString)) {
			return "Value must contain only the characters 0-9 and A-F";
		}
		return null;
	}
	
	@Override
	public int getMaxLength() {
		return lengthInCharacters;
	}
	
	public List<DecodedData> decode(String bitString, int startIndexInBytes, DecodeSession decodeSession) {
		long bitStringAsLong = Long.parseLong(bitString, 16);
		ArrayList<DecodedData> bitsSetInString = new ArrayList<DecodedData>();
		for (BitMapping bit : bits) {
			if (bit.isSet(bitStringAsLong)) {
				int start = startIndexInBytes + bit.getByteIndex();
				bitsSetInString.add(new DecodedData(bit.getBitAsHexString(), bit.getDescription(), start, start + 1));
			}
		}
		return bitsSetInString;
	}
	
	private static class BitMapping {
		private final long bit;
		private final String bitAsHexString;
		private final String description;
		private final int byteIndex;
		
		public BitMapping(String bitAsHexString, String description) {
			this.bit = Long.parseLong(bitAsHexString, 16);
			this.bitAsHexString = bitAsHexString;
			this.description = description;
			this.byteIndex = findNonZeroByte(bitAsHexString);
		}
		
		private int findNonZeroByte(String bitAsHexString) {
			byte[] bytes = ISOUtil.hex2byte(bitAsHexString);
			for (int i = 0; i < bytes.length; i++) {
				if (bytes[i] != 0) {
					return i;
				}
			}
			throw new IllegalArgumentException("No non-zero bits in " + bitAsHexString);
		}

		public boolean isSet(long bitString) {
			return (bitString & bit) == bit;
		}
		
		public String getBitAsHexString() {
			return bitAsHexString;
		}
		
		public String getDescription() {
			return description;
		}
		
		public int getByteIndex() {
			return byteIndex;
		}
	}
}
