package io.github.binaryfoo.decoders;

public class CardTxQualifiersDecoder extends EmvBitStringDecoder {
    public CardTxQualifiersDecoder() {
        super("fields/card-tx-qualifiers.txt", true);
    }
}
