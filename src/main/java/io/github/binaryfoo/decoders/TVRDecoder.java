package io.github.binaryfoo.decoders;

public class TVRDecoder extends EmvBitStringDecoder {

	public TVRDecoder() {
        super("fields/tvr.txt", true);
    }
}
