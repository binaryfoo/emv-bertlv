package io.github.binaryfoo.decoders;

import io.github.binaryfoo.DecodedData;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class TVRDecoderTest {

  private TVRDecoder decoder;

  @Before
  public void setup() {
    decoder = new TVRDecoder();
  }

  @Test
  public void testPinRequiredButNotEntered() {
    List<DecodedData> expected = Arrays.asList(DecodedData.primitive("0000080000 (Byte 3 Bit 4)", "PIN entry required, PIN pad present, but PIN was not entered", 2, 3));
    List<DecodedData> actual = decoder.decode("0000080000", 0, new DecodeSession());
    assertEquals(expected, actual);
  }

  @Test
  public void testAllBitsSet() throws Exception {
    List<DecodedData> actual = decoder.decode("FFFFFFFFFF", 0, new DecodeSession());
    assertEquals(27, actual.size());
  }
}
