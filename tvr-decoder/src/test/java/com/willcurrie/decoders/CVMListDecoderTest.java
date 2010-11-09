package com.willcurrie.decoders;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.willcurrie.DecodedData;


public class CVMListDecoderTest {
	
	private CVMListDecoder decoder;

	@Before
	public void setup() {
		decoder = new CVMListDecoder();
	}
	
	@Test
	public void testListWithXBasedRule() {
		List<DecodedData> expected = Arrays.asList(
				new DecodedData("0207", "Encrytped PIN online, If transaction in application currency and >= X, FAIL (x = 200)", 8, 10),
				new DecodedData("1E00", "Signature, Always, FAIL", 10, 12)
		);
		List<DecodedData> actual = decoder.decode("000000C80000000002071E00", 0);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testValidate() {
		assertEquals("Value must be at least 16 characters", decoder.validate("00"));
		assertEquals("Length must be a multiple of 4", decoder.validate("000000000000000011"));
		assertEquals("Value must contain only the characters 0-9 and A-F", decoder.validate("000000000000000011xx"));
		assertEquals(null, decoder.validate("0000000000000000"));
		assertEquals(null, decoder.validate("000000C80000000002071E00"));
	}
}
