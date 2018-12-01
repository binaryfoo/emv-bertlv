package io.github.binaryfoo.decoders.apdu;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ResponseCodeTest {

  @Test
  public void decode() {
    assertThat(ResponseCode.lookup("9000").getDescription(), is("OK"));
  }
}
