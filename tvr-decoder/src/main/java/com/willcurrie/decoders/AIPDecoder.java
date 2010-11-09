package com.willcurrie.decoders;

import com.willcurrie.FixedLengthDecoder;

public class AIPDecoder extends FixedLengthDecoder {

	public AIPDecoder() {
		super(4,
		  "4000", "SDA supported",
          "2000", "DDA supported",
          "1000", "Cardholder verification is supported",
          "0800", "Terminal risk management is to be performed",
          "0400", "Issuer authentication is supported",
          "0100", "CDA supported"
		);
	}
}
