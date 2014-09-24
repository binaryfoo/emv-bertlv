package io.github.binaryfoo.decoders;

public class AmexTerminalTxCapabilitiesDecoder extends FixedLengthDecoder {
    public AmexTerminalTxCapabilitiesDecoder() {
        super(8,
                "80000000", "AEIPS contact mode supported",
                "40000000", "Expresspay Magstripe Mode supported",
                "20000000", "Expresspay EMV full online mode supported",
                "10000000", "Expresspay EMV partial online mode supported",
                "08000000", "Expresspay Mobile Supported",
                "02000000", "Signature supported",
                "00800000", "Mobile CVM supported",
                "00400000", "Online PIN supported",
                "00200000", "Signature",
                "00100000", "Plaintext Offline PIN",
                "00008000", "Terminal is offline only",
                "00004000", "CVM Required"
                );
    }

}
