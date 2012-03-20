package com.willcurrie.decoders;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.willcurrie.DecodedData;


public class CVMResultsDecoderTest {

	@Test
	public void testDecode() {
		CVMResultsDecoder decoder = new CVMResultsDecoder();
		List<DecodedData> expected = Arrays.asList(
				new DecodedData("1E", "Signature", 0, 1),
				new DecodedData("00", "Always", 1, 2),
				new DecodedData("02", "Sucessful", 2, 3)
				);
		List<DecodedData> actual = decoder.decode("1E0002", 0, new DecodeSession());
		assertEquals(expected, actual);
	}
	
	@Test
	public void testValidate() {
		CVMResultsDecoder decoder = new CVMResultsDecoder();
		assertEquals("Value must be exactly 6 characters", decoder.validate("00"));
		assertEquals("Value must be exactly 6 characters", decoder.validate("0000000"));
		assertEquals("Value must contain only the characters 0-9 and A-F", decoder.validate("0011xx"));
		assertEquals(null, decoder.validate("000000"));
	}
}
