package com.willcurrie.decoders;

import java.util.ArrayList;
import java.util.List;

import com.willcurrie.*;
import com.willcurrie.tlv.BerTlv;
import com.willcurrie.tlv.ISOUtil;
import com.willcurrie.tlv.Tag;

public class TLVDecoder implements Decoder {

	@Override
	public List<DecodedData> decode(String input, int startIndexInBytes, DecodeSession decodeSession) {
		List<BerTlv> list = BerTlv.parseList(ISOUtil.hex2byte(input), true);
		return decodeTlvs(list, startIndexInBytes, decodeSession);
	}

	private List<DecodedData> decodeTlvs(List<BerTlv> list, int startIndex, DecodeSession decodeSession) {
		ArrayList<DecodedData> decodedItems = new ArrayList<DecodedData>();
		for (BerTlv berTlv : list) {
			String valueAsHexString = berTlv.getValueAsHexString();
			Tag tag = berTlv.getTag();
			int length = berTlv.toBinary().length;
			int contentEndIndex = startIndex + length;
			int compositeStartElementIndex = startIndex + tag.getBytes().length + berTlv.getLengthInBytesOfEncodedLength();
            TagMetaData tagMetaData = decodeSession.getTagMetaData();
            if (tag.isConstructed()) {
                decodedItems.add(new DecodedData(tag, tag.toString(tagMetaData), valueAsHexString, startIndex, contentEndIndex, decodeTlvs(berTlv.getChildren(), compositeStartElementIndex, decodeSession)));
            } else {
                TagInfo tagInfo = tagMetaData.get(tag);
                decodedItems.add(new DecodedData(tag, tag.toString(tagMetaData), tagInfo.decodePrimitiveTlvValue(valueAsHexString), startIndex, contentEndIndex, tagInfo.getDecoder().decode(valueAsHexString, compositeStartElementIndex, decodeSession)));
            }
			startIndex += length;
		}
		return decodedItems;
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
