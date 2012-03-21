package com.willcurrie;

import java.util.HashMap;
import java.util.Map;

import com.willcurrie.decoders.*;
import com.willcurrie.decoders.apdu.*;
import com.willcurrie.tlv.Tag;

public class TagInfo {
    private final String shortName;
	private final String longName;
	private final Decoder decoder;
    private final PrimitiveDecoder primitiveDecoder;

    public TagInfo(String shortName, String longName, Decoder decoder) {
        this(shortName, longName, decoder, new NullPrimitiveDecoder());
    }
    
    TagInfo(String shortName, String longName, Decoder decoder, PrimitiveDecoder primitiveDecoder) {
        this.shortName = shortName;
		this.longName = longName;
		this.decoder = decoder;
        this.primitiveDecoder = primitiveDecoder;
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

    public String decodePrimitiveTlvValue(String valueAsHexString) {
        return primitiveDecoder.decode(valueAsHexString);
    }

}
