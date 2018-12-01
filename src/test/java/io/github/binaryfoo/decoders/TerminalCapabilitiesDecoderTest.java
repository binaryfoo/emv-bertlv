package io.github.binaryfoo.decoders;

import io.github.binaryfoo.DecodedData;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class TerminalCapabilitiesDecoderTest {

  private TerminalCapabilitiesDecoder decoder;

  @Before
  public void setup() {
    decoder = new TerminalCapabilitiesDecoder();
  }

  @Test
  public void testNoCVMRequired() {
    List<DecodedData> expected = Arrays.asList(DecodedData.primitive("000800 (Byte 2 Bit 4)", "No CVM Required", 1, 2));
    List<DecodedData> actual = decoder.decode("000800", 0, new DecodeSession());
    assertEquals(expected, actual);
  }

  @Test
  public void test2() {
    List<DecodedData> expected = Arrays.asList(
        DecodedData.primitive("400000 (Byte 1 Bit 7)", "Magnetic stripe", 0, 1),
        DecodedData.primitive("200000 (Byte 1 Bit 6)", "IC with contacts", 0, 1),
        DecodedData.primitive("008000 (Byte 2 Bit 8)", "Plaintext PIN for ICC verification", 1, 2),
        DecodedData.primitive("001000 (Byte 2 Bit 5)", "Enciphered PIN for offline verification", 1, 2),
        DecodedData.primitive("000800 (Byte 2 Bit 4)", "No CVM Required", 1, 2),
        DecodedData.primitive("000080 (Byte 3 Bit 8)", "SDA", 2, 3),
        DecodedData.primitive("000040 (Byte 3 Bit 7)", "DDA", 2, 3),
        DecodedData.primitive("000008 (Byte 3 Bit 4)", "CDA", 2, 3));

    List<DecodedData> actual = decoder.decode("6098C8", 0, new DecodeSession());
    assertEquals(expected, actual);
  }

  @Test
  public void testAllBitsSet() throws Exception {
    List<DecodedData> actual = decoder.decode("FFFFFF", 0, new DecodeSession());
    assertEquals(24, actual.size());
  }
}
