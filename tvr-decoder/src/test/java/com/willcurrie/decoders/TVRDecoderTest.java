package com.willcurrie.decoders;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.willcurrie.DecodedData;


public class TVRDecoderTest {

	private TVRDecoder decoder;
	
	@Before
	public void setup() {
		decoder = new TVRDecoder();
	}
	
	@Test
	public void testPinRequiredButNotEntered() {
		List<DecodedData> expected = Arrays.asList(new DecodedData("0000080000", "PIN entry required, PIN pad present, but PIN was not entered", 2, 3));
		List<DecodedData> actual = decoder.decode("0000080000", 0);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testAllBitsSet() throws Exception {
		List<DecodedData> actual = decoder.decode("FFFFFFFFFF", 0);
		assertEquals(26, actual.size());
	}
}
