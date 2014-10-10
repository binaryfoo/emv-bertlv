package io.github.binaryfoo.decoders;

public class TerminalTxQualifiersDecoder extends EmvBitStringDecoder {
    public TerminalTxQualifiersDecoder() {
        super("fields/terminal-tx-qualifiers.txt", true);
    }
}
