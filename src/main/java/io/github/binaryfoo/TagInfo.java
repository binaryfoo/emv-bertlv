package io.github.binaryfoo;

import io.github.binaryfoo.decoders.*;

/**
 * How a single tag should be decoded.
 */
public class TagInfo {
    private final String shortName;
	private final String longName;
	private final Decoder decoder;
    private final PrimitiveDecoder primitiveDecoder;

    public TagInfo(String shortName, String longName, Decoder decoder) {
        this(shortName, longName, decoder, PrimitiveDecoder.HEX);
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

    /**
     * Decode the value of a BerTLv (primitive or constructed) to be included in the children field of a DecodedData.
     *
     * Idea being children are shown in a tree structure.
     */
    public Decoder getDecoder() {
		return decoder;
	}

	public int getMaxLength() {
		return decoder.getMaxLength();
	}

    /**
     * Decode the value of a PrimitiveBerTLv to be included in the decodedData field of a DecodedData.
     *
     * Idea being the decodedData is shown on the same line (same node in the tree) instead of nesting one level down.
     */
    public String decodePrimitiveTlvValue(String valueAsHexString) {
        return primitiveDecoder.decode(valueAsHexString);
    }

}
