package com.willcurrie.decoders;

public class TerminalTxQualifiersDecoder extends FixedLengthDecoder {
    public TerminalTxQualifiersDecoder() {
        super(8,
                "80000000", "MSD supported",
                "20000000", "qVSDC supported",
                "10000000", "EMV contact chip supported",
                "08000000", "Offline only reader",
                "04000000", "Online PIN supported",
                "02000000", "Signature supported",
                "01000000", "Offline data auth for online transactions supported",
                "00800000", "Online cryptogram required",
                "00400000", "CVM required",
                "00200000", "Contact chip offline pin supported",
                "00008000", "Issuer update processing supported",
                "00004000", "Mobile device functionality supported"
                );
    }

}
