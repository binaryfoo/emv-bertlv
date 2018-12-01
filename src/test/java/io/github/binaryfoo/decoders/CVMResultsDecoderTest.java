package io.github.binaryfoo.decoders;

import io.github.binaryfoo.DecodedData;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class CVMResultsDecoderTest {

  @Test
  public void testDecode() {
    CVMResultsDecoder decoder = new CVMResultsDecoder();
    List<DecodedData> expected = Arrays.asList(
        DecodedData.primitive("1E", "Signature", 0, 1),
        DecodedData.primitive("00", "Always", 1, 2),
        DecodedData.primitive("02", "Sucessful", 2, 3)
    );
    List<DecodedData> actual = decoder.decode("1E0002", 0, new DecodeSession());
    assertEquals(expected, actual);
  }

  @Test
  public void testDecodeInvalidValue() throws Exception {
    CVMResultsDecoder decoder = new CVMResultsDecoder();
    List<DecodedData> expected = Arrays.asList(
        DecodedData.primitive("3F", "Unknown", 0, 1),
        DecodedData.primitive("00", "Always", 1, 2),
        DecodedData.primitive("02", "Sucessful", 2, 3)
    );
    List<DecodedData> actual = decoder.decode("3F0002", 0, new DecodeSession());
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
