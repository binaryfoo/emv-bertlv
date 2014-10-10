package io.github.binaryfoo.decoders;

public class AmexTerminalTxCapabilitiesDecoder extends EmvBitStringDecoder {
    public AmexTerminalTxCapabilitiesDecoder() {
        super("fields/amex-terminal-tx-capabilities.txt", true);
    }
}
