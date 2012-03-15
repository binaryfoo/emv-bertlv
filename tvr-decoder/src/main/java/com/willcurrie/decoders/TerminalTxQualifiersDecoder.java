package com.willcurrie.decoders;

import com.willcurrie.FixedLengthDecoder;

public class TerminalTxQualifiersDecoder extends FixedLengthDecoder {
    public TerminalTxQualifiersDecoder() {
        super(6,
                "800000", "MSD supported",
                "200000", "qVSDC supported",
                "100000", "EMV contact chip supported",
                "080000", "Offline only reader",
                "040000", "Online PIN supported",
                "020000", "Signature supported",
                "010000", "Offline data auth for online transactions supported",
                "008000", "Online cryptogram required",
                "004000", "CVM required",
                "002000", "Contact chip offline pin supported",
                "000080", "Issuer update processing supported",
                "000040", "Mobile device functionality supported"
                );
    }

}
