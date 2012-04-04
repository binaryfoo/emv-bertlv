package com.willcurrie.decoders;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class FixedLengthDecoderTest {

	@Test
	public void testValidate() {
		FixedLengthDecoder decoder = new FixedLengthDecoder(5);
		String lengthErrorMessage = "Value must be exactly 5 characters";
		assertEquals(lengthErrorMessage, decoder.validate("000"));
		assertEquals(lengthErrorMessage, decoder.validate("000000"));
		assertEquals(lengthErrorMessage, decoder.validate(null));
		assertEquals(lengthErrorMessage, decoder.validate(""));
		
		String charactersErrorMessage = "Value must contain only the characters 0-9 and A-F";
		assertEquals(charactersErrorMessage, decoder.validate("xxxxx"));
		
		assertEquals(null, decoder.validate("00000"));
		assertEquals(null, decoder.validate("11111"));
		assertEquals(null, decoder.validate("fffff"));
	}
}
