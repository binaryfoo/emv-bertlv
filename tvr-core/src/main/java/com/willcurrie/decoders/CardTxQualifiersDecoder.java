package com.willcurrie.decoders;

public class CardTxQualifiersDecoder extends FixedLengthDecoder {
    public CardTxQualifiersDecoder() {
        super(4,
                "8000", "Online PIN required",
                "4000", "Signature required",
                "2000", "Go online if offline data auth fails and reader is online capable",
                "1000", "Switch interface if offline data auth fails and reader suports VIS",
                "0800", "Go online if application expired",
                "0400", "Switch interface for cash",
                "0200", "Switch interface for cashback",
                "0080", "Consumer device CVM performed",
                "0040", "Card supports issuer update processing at the POS");
    }

}
