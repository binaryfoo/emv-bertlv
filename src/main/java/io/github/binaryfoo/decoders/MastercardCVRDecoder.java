package io.github.binaryfoo.decoders;

public class MastercardCVRDecoder extends EmvBitStringDecoder {
    public MastercardCVRDecoder() {
        super("fields/mastercard-cvr.txt", false);
    }
}
