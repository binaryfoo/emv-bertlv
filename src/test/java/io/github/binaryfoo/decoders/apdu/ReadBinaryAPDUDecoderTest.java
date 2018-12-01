package io.github.binaryfoo.decoders.apdu;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.decoders.DecodeSession;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ReadBinaryAPDUDecoderTest {

  @Test
  public void testDecode() throws Exception {
    DecodedData decoded = new ReadBinaryAPDUDecoder().decode("00B0000200", 0, new DecodeSession());
    assertThat(decoded.getRawData(), is("C-APDU: Read Binary"));
    assertThat(decoded.getDecodedData(), is("P1=0 P2=2"));
  }
}
