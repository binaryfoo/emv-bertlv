package com.willcurrie.decoders;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import com.willcurrie.tlv.Tag;
import org.junit.Test;

import com.willcurrie.DecodedData;


public class TLVDecoderTest {

	@Test
	public void testDecode() {
		TLVDecoder decoder = new TLVDecoder();
		List<DecodedData> expected = Arrays.asList(
				new DecodedData(Tag.fromHex("E0"), "9F020600000000500082025C009F360200019F260886087FD3A9FE6FB49F100706010A03A010009F3303E0F0C09F1A0200365F2A0200369F1E08424B00081905CB9F950500000080009A030809229C01009F37041035EA1F8A00", 0, 1)
				);
		List<DecodedData> actual = decoder.decode("E05A9F020600000000500082025C009F360200019F260886087FD3A9FE6FB49F100706010A03A010009F3303E0F0C09F1A0200365F2A0200369F1E08424B00081905CB9F950500000080009A030809229C01009F37041035EA1F8A00", 0);
		assertEquals(expected, actual);
	}
}
