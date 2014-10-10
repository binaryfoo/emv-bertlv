package io.github.binaryfoo.decoders;

public class AIPDecoder extends EmvBitStringDecoder {

	public AIPDecoder() {
		super("fields/aip.txt", true);
	}
}
