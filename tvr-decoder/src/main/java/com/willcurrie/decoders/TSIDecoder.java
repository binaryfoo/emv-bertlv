package com.willcurrie.decoders;

import com.willcurrie.FixedLengthDecoder;

public class TSIDecoder extends FixedLengthDecoder {

	public TSIDecoder() {
		super(4,
			"8000", "Offline data authentication was performed",
		    "4000", "Cardholder verification was performed",
		    "2000", "Card risk management was performed",
		    "1000", "Issuer authentication was performed",
		    "0800", "Terminal risk management was performed",
		    "0400", "Script processing was performed"
		    );
	}
}
