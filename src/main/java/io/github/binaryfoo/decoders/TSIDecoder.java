package io.github.binaryfoo.decoders;

public class TSIDecoder extends EmvBitStringDecoder {

	public TSIDecoder() {
		super("fields/tsi.txt", true);
	}
}
