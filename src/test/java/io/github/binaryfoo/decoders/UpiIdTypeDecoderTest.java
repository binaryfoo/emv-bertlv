package io.github.binaryfoo.decoders;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UpiIdTypeDecoderTest {

  private UpiIdTypeDecoder decoder = new UpiIdTypeDecoder();

  @Test
  public void decode() {
    assertThat(decoder.decode("00"), is("ID card"));
    assertThat(decoder.decode("02"), is("Passport"));
    assertThat(decoder.decode("42"), is("Unknown"));
  }
}
