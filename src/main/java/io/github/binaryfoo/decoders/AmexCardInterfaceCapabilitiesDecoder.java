package io.github.binaryfoo.decoders;

public class AmexCardInterfaceCapabilitiesDecoder extends EmvBitStringDecoder {
    public AmexCardInterfaceCapabilitiesDecoder() {
        super("fields/amex-card-interface-capabilities.txt", true);
    }
}
