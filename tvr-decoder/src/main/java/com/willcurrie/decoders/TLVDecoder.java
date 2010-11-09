package com.willcurrie.decoders;

import java.util.ArrayList;
import java.util.List;

import com.willcurrie.DecodedData;
import com.willcurrie.Decoder;
import com.willcurrie.EmvTags;
import com.willcurrie.TagInfo;
import com.willcurrie.tlv.BerTlv;
import com.willcurrie.tlv.ISOUtil;
import com.willcurrie.tlv.Tag;

public class TLVDecoder implements Decoder {

	@Override
	public List<DecodedData> decode(String input, int startIndex) {
		List<BerTlv> list = BerTlv.parseList(ISOUtil.hex2byte(input));
		return decodeTlvs(list, startIndex);
	}

	private List<DecodedData> decodeTlvs(List<BerTlv> list, int startIndex) {
		ArrayList<DecodedData> decodedItems = new ArrayList<DecodedData>();
		for (BerTlv berTlv : list) {
			String valueAsHexString = berTlv.getValueAsHexString();
			Tag tag = berTlv.getTag();
			int length = berTlv.toBinary().length;
			int contentEndIndex = startIndex + length;
			int compositeStartElementIndex = startIndex + tag.getBytes().length + berTlv.getLengthInBytesOfEncodedLength();
			if (tag.isConstructed()) {
				decodedItems.add(new DecodedData(tag.toString(), valueAsHexString, startIndex, contentEndIndex, decodeTlvs(berTlv.getChildren(), compositeStartElementIndex)));
			} else if (TagInfo.get(tag.getHexString()) != null) {
				TagInfo tagInfo = TagInfo.get(tag.getHexString());
				decodedItems.add(new DecodedData(tag.toString(), valueAsHexString, startIndex, contentEndIndex, tagInfo.getDecoder().decode(valueAsHexString, compositeStartElementIndex)));
			} else {
				decodedItems.add(new DecodedData(tag.toString(), decodePrimitiveTlv(berTlv), startIndex, contentEndIndex));
			}
			startIndex += length;
		}
		return decodedItems;
	}

	private String decodePrimitiveTlv(BerTlv berTlv) {
		if (EmvTags.isTagAscii(berTlv.getTag())) {
		    return ISOUtil.dumpString(berTlv.getValue());
		} 
		return berTlv.getValueAsHexString();
	}
	
	@Override
	public int getMaxLength() {
		return 10000;
	}

	@Override
	public String validate(String input) {
		if (input == null || input.length() < 2) {
			return String.format("Value must be at least %d characters", 2); 
		}
		if (input.length() % 2 != 0) {
			return "Length must be a multiple of 2";
		}
		if (!ISOUtil.isValidHexString(input)) {
			return "Value must contain only the characters 0-9 and A-F";
		}
		return null;
	}

}
