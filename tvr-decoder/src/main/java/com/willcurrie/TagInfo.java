package com.willcurrie;

import java.util.HashMap;
import java.util.Map;

import com.willcurrie.decoders.AIPDecoder;
import com.willcurrie.decoders.CVMListDecoder;
import com.willcurrie.decoders.CVMResultsDecoder;
import com.willcurrie.decoders.TLVDecoder;
import com.willcurrie.decoders.TSIDecoder;
import com.willcurrie.decoders.TVRDecoder;

public class TagInfo {
	private final String tag;
	private final String shortName;
	private final String longName;
	private final Decoder decoder;
	
	private TagInfo(String tag, String shortName, String longName, Decoder decoder) {
		this.tag = tag;
		this.shortName = shortName;
		this.longName = longName;
		this.decoder = decoder;
	}
	
	public String getTag() {
		return tag;
	}

	public String getShortName() {
		return shortName;
	}

	public String getLongName() {
		return longName;
	}

	public Decoder getDecoder() {
		return decoder;
	}
	
	public int getMaxLength() {
		return decoder.getMaxLength();
	}
	
	private static final TagInfo[] TAGS = {
		new TagInfo("95", "TVR", "Terminal Verification Results", new TVRDecoder()),
		new TagInfo("9B", "TSI", "Transaction Status Indicator", new TSIDecoder()),
		new TagInfo("82", "AIP", "Application Interchange Profile", new AIPDecoder()),
		new TagInfo("8E", "CVM List", "Cardholder Verification Method List", new CVMListDecoder()),
		new TagInfo("9F34", "CVM Results", "Cardholder Verification Results", new CVMResultsDecoder()),
		new TagInfo("constructed", "TLV Data", "Constructed TLV data", new TLVDecoder()),
	};
	private static final Map<String, TagInfo> TAG_LOOKUP = new HashMap<String, TagInfo>();
	static {
		for (TagInfo tagInfo : TAGS) {
			TagInfo previous = TAG_LOOKUP.put(tagInfo.getTag(), tagInfo);
			assert previous == null : "Duplicate value in TAGS for " + previous.getTag();
		}
	}
	
	public static TagInfo get(String tagHexString) {
		return TAG_LOOKUP.get(tagHexString);
	}
}
