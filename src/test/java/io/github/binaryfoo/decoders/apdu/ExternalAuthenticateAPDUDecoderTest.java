package io.github.binaryfoo.decoders.apdu;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.decoders.DecodeSession;
import org.junit.Test;

import static io.github.binaryfoo.BoundsMatcher.hasBounds;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ExternalAuthenticateAPDUDecoderTest {
  @Test
  public void testDecode() throws Exception {
    String input = "008200000A0EC3EE4C45E247893030";
    int startIndex = 4;
    DecodeSession session = new DecodeSession();
    DecodedData decoded = new ExternalAuthenticateAPDUDecoder().decode(input, startIndex, session);
    assertThat(decoded.getRawData(), is("C-APDU: External Authenticate"));
    assertThat(decoded.getDecodedData(), is("0EC3EE4C45E247893030"));
    assertThat(decoded, hasBounds(startIndex, startIndex + 15));

    assertThat(decoded.getChild(0).getRawData(), is("ARPC"));
    assertThat(decoded.getChild(0).getDecodedData(), is("0EC3EE4C45E24789"));
    assertThat(decoded.getChild(0), hasBounds(startIndex + 5, startIndex + 5 + 8));
    assertThat(decoded.getChild(1).getRawData(), is("Issuer Specific"));
    assertThat(decoded.getChild(1).getDecodedData(), is("3030"));
    assertThat(decoded.getChild(1), hasBounds(startIndex + 5 + 8, startIndex + 5 + 8 + 2));
  }
}
