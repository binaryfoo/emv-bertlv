package io.github.binaryfoo.decoders;

import io.github.binaryfoo.DecodedData;
import org.junit.Test;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

public class MobileCVMDecoderTest {

  private MobileCVMDecoder decoder = new MobileCVMDecoder();

  @Test
  public void testMobileCVMPerformed() {
    assertThat(decoder.decode("010000", 0, new DecodeSession()), hasItem(DecodedData.primitive("Byte 1 = 0x01", "Mobile CVM Performed", 0, 1)));
  }

  @Test
  public void testNoCVMPerformed() {
    assertThat(decoder.decode("3F0000", 0, new DecodeSession()), hasItem(DecodedData.primitive("Byte 1 = 0x3F", "No CVM Performed", 0, 1)));
  }

  @Test
  public void testCVMCondition_NotRequired() {
    assertThat(decoder.decode("000000", 0, new DecodeSession()), hasItem(DecodedData.primitive("Byte 2 = 0x00", "Mobile CVM Not Required", 1, 2)));
  }

  @Test
  public void testCVMCondition_Required() {
    assertThat(decoder.decode("000300", 0, new DecodeSession()), hasItem(DecodedData.primitive("Byte 2 = 0x03", "Terminal Required CVM", 1, 2)));
  }

  @Test
  public void testCVMResults_Failed() {
    assertThat(decoder.decode("000001", 0, new DecodeSession()), hasItem(DecodedData.primitive("Byte 3 = 0x01", "Mobile CVM Failed", 2, 3)));
  }

  @Test
  public void testCVMResults_Successful() {
    assertThat(decoder.decode("000002", 0, new DecodeSession()), hasItem(DecodedData.primitive("Byte 3 = 0x02", "Mobile CVM Successful", 2, 3)));
  }

  @Test
  public void testCVMResults_Blocked() {
    assertThat(decoder.decode("000003", 0, new DecodeSession()), hasItem(DecodedData.primitive("Byte 3 = 0x03", "Mobile CVM Blocked", 2, 3)));
  }
}
