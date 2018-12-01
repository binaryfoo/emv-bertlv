package io.github.binaryfoo.decoders;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SometimesAsciiPrimitiveDecoderTest {

  @Test
  public void decodesAscii() {
    String decoded = new SometimesAsciiPrimitiveDecoder().decode("315041592E5359532E4444463031");
    assertThat(decoded, is("1PAY.SYS.DDF01"));
  }

  @Test
  public void puntsToHexWhenNotAscii() {
    String decoded = new SometimesAsciiPrimitiveDecoder().decode("A000000025010801");
    assertThat(decoded, is("A000000025010801"));
  }
}
