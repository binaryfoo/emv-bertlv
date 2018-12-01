package io.github.binaryfoo.decoders.apdu;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.decoders.DecodeSession;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class GetChallengeAPDUDecoderTest {
  @Test
  public void testDecode() {
    String input = "0020000000";
    int startIndex = 3;
    DecodeSession session = new DecodeSession();
    DecodedData decodedData = new GetChallengeAPDUDecoder().decode(input, startIndex, session);
    assertThat(decodedData.getRawData(), is("C-APDU: Get Challenge"));
    assertThat(decodedData.getDecodedData(), is(""));
    assertThat(decodedData.getStartIndex(), is(startIndex));
    assertThat(decodedData.getEndIndex(), is(startIndex + 5));
  }
}
