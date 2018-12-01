package io.github.binaryfoo.decoders.apdu;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.EmvTags;
import io.github.binaryfoo.decoders.DecodeSession;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class GetDataAPDUDecoderTest {
  @Test
  public void testDecode() throws Exception {
    String input = "80CA9F1700";
    int startIndex = 3;
    DecodeSession session = new DecodeSession();
    session.setTagMetaData(EmvTags.METADATA);
    DecodedData decodedData = new GetDataAPDUDecoder().decode(input, startIndex, session);
    assertThat(decodedData.getRawData(), is("C-APDU: GetData"));
    assertThat(decodedData.getDecodedData(), is("9F17 (pin try counter)"));
    assertThat(decodedData.getStartIndex(), is(startIndex));
    assertThat(decodedData.getEndIndex(), is(startIndex + 5));
  }
}
