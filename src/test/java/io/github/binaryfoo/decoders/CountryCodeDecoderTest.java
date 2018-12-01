package io.github.binaryfoo.decoders;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CountryCodeDecoderTest {

  private CountryCodeDecoder decoder = new CountryCodeDecoder();

  @Test
  public void decodeCountries() throws Exception {
    assertThat(decoder.decode("0036"), is("AUS (Australia)"));
    assertThat(decoder.decode("0203"), is("CZE (Czech Republic)")); // name with space
    assertThat(decoder.decode("0275"), is("PSE (Palestine, State of)")); // name with comma
  }
}
