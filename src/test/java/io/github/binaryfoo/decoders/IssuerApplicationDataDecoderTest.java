package io.github.binaryfoo.decoders;

import io.github.binaryfoo.DecodedData;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class IssuerApplicationDataDecoderTest {
  private IssuerApplicationDataDecoder decoder;

  @Before
  public void setUp() {
    decoder = new IssuerApplicationDataDecoder();
  }

  @Test
  public void testDecodeVisaIAD() {
    String input = "06010A03A4B8000F112233445566778899AABBCCDDEEFF";
    int startIndex = 5;
    DecodeSession session = new DecodeSession();
    List<DecodedData> decodedData = decoder.decode(input, startIndex, session);
    assertThat(decodedData.get(0), is(DecodedData.primitive("Derivation key index", "01", startIndex + 1, startIndex + 2)));
    assertThat(decodedData.get(1), is(DecodedData.primitive("Cryptogram version number", "0A", startIndex + 2, startIndex + 3)));
    assertThat(decodedData.get(2), is(DecodedData.constructed("Card verification results", "03A4B800", startIndex + 3, startIndex + 7, expectedCvrChildrenFor("03A4B800"))));
    assertThat(decodedData.get(3), is(DecodedData.primitive("Issuer discretionary data", "112233445566778899AABBCCDDEEFF", startIndex + 8, startIndex + 16)));
    assertThat(decodedData.size(), is(4));
  }

  @Test
  public void testDecodeVisaIADWithoutIssuerDiscretionaryData() {
    String input = "06010A03A00000";
    int startIndex = 5;
    DecodeSession session = new DecodeSession();
    List<DecodedData> decodedData = decoder.decode(input, startIndex, session);
    assertThat(decodedData.get(0), is(DecodedData.primitive("Derivation key index", "01", startIndex + 1, startIndex + 2)));
    assertThat(decodedData.get(1), is(DecodedData.primitive("Cryptogram version number", "0A", startIndex + 2, startIndex + 3)));
    assertThat(decodedData.get(2), is(DecodedData.constructed("Card verification results", "03A00000", startIndex + 3, startIndex + 7, expectedCvrChildrenFor("03A00000"))));
    assertThat(decodedData.size(), is(3));
  }

  @Test
  public void decodeJcbValue() {
    List<DecodedData> decoded = decoder.decode("0701010460040280", 0, new DecodeSession());
    assertThat(decoded.get(2).getRawData(), is("Card verification results"));
    assertThat(decoded.get(2).getDecodedData(), is("0460040280"));
  }

  @Test
  public void decodeMasterCardValue() {
    List<DecodedData> decoded = decoder.decode("0110A78005022000693800000001999300FF", 0, new DecodeSession());
    assertThat(decoded.get(2).getRawData(), is("Card verification results"));
    assertThat(decoded.get(2).getDecodedData(), is("A78005022000"));
    assertThat(decoded.get(3).getRawData(), is("DAC/ICC Dynamic Number 2 Bytes"));
    assertThat(decoded.get(3).getDecodedData(), is("6938"));
    assertThat(decoded.get(4).getRawData(), is("Plaintext/Encrypted Counters"));
    assertThat(decoded.get(4).getDecodedData(), is("00000001999300FF"));
  }

  @Test
  public void decodeMasterCardValue2() {
    List<DecodedData> decoded = decoder.decode("0110A08001222000067500000000000000FF00000000000000FF", 0, new DecodeSession());
    assertThat(decoded.get(2).getRawData(), is("Card verification results"));
    assertThat(decoded.get(2).getDecodedData(), is("A08001222000"));
  }

  private List<DecodedData> expectedCvrChildrenFor(String input) {
    return new VisaCardVerificationResultsDecoder().decode(input, 5 + 3, new DecodeSession());
  }
}
