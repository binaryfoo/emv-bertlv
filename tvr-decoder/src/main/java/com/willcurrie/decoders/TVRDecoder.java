package com.willcurrie.decoders;

import com.willcurrie.FixedLengthDecoder;

public class TVRDecoder extends FixedLengthDecoder {

	public TVRDecoder() {
		super(10,
			"8000000000", "Offline data authentication was not performed",
			"4000000000", "SDA failed",
			"2000000000", "ICC data missing",
			"1000000000", "Card appears on terminal exception file",
			"0800000000", "DDA failed",
			"0400000000", "CDA failed",
			"0080000000", "ICC and terminal have different application versions",
			"0040000000", "Expired application",
			"0020000000", "Application not yet effective",
			"0010000000", "Requested service not allowed for card product",
			"0008000000", "New card",
			"0000800000", "Cardholder verification was not successful",
			"0000400000", "Unrecognised CVM",
			"0000200000", "PIN try limit exceeded",
			"0000100000", "PIN entry required and PIN pad not present or not working",
			"0000080000", "PIN entry required, PIN pad present, but PIN was not entered",
			"0000040000", "Online PIN entered",
			"0000008000", "Transaction exceeds floor limit",
			"0000004000", "Lower consecutive offline limit exceeded",
			"0000002000", "Upper consecutive offline limit exceeded",
			"0000001000", "Transaction selected randomly for online processing",
			"0000000800", "Merchant forced transaction online",
			"0000000080", "Default TDOL used",
			"0000000040", "Issuer authentication failed",
			"0000000020", "Script processing failed before final GENERATE AC",
			"0000000010", "Script processing failed after final GENERATE AC"
		  );
	}
}
