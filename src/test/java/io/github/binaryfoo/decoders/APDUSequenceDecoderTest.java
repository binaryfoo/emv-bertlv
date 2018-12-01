package io.github.binaryfoo.decoders;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.Decoder;
import io.github.binaryfoo.EmvTags;
import io.github.binaryfoo.decoders.apdu.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static io.github.binaryfoo.BoundsMatcher.hasBounds;
import static io.github.binaryfoo.DecodedMatcher.decodedAs;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class APDUSequenceDecoderTest {

  private DecodeSession session;
  private Decoder decoder;

  @Before
  public void setUp() {
    ReplyAPDUDecoder replyAPDUDecoder = new ReplyAPDUDecoder(new TLVDecoder());
    decoder = new APDUSequenceDecoder(replyAPDUDecoder,
        new SelectCommandAPDUDecoder(),
        new GetProcessingOptionsCommandAPDUDecoder(),
        new InternalAuthenticateAPDUDecoder());
    session = new DecodeSession();
    session.setTagMetaData(EmvTags.METADATA);
  }

  @Test
  public void testOneSelectCommand() {
    String input = "00A4040007A000000004101000";

    List<DecodedData> list = decoder.decode(input, 0, session);
    assertThat(list.size(), is(1));
    assertThat(list.get(0), is(decodedAs("C-APDU: Select", "AID A0000000041010")));
    assertThat(list.get(0), hasBounds(0, input.length() / 2));
    assertThat(list.get(0).getHexDump(), is(notNullValue()));
  }

  @Test
  public void testOneGetProcessingOptionsCommand() {
    String input = "80A8000002830000";

    List<DecodedData> list = decoder.decode(input, 0, session);
    assertThat(list.size(), is(1));
    assertThat(list.get(0), is(decodedAs("C-APDU: GPO", "No PDOL included")));
    assertThat(list.get(0), hasBounds(0, input.length() / 2));
    assertThat(list.get(0).getHexDump(), is(notNullValue()));
  }

  @Test
  public void testOneSelectCommandPlusResponse() {
    String line1 = "00A4040007A000000004101000";
    String line2 = "6F1C8407A0000000041010A511500F505043204D434420303420207632309000";
    String input = line1 + " " + line2;

    List<DecodedData> list = decoder.decode(input, 0, session);
    assertThat(list.size(), is(2));
    assertThat(list.get(0), is(decodedAs("C-APDU: Select", "AID A0000000041010")));
    assertThat(list.get(0), hasBounds(0, line1.length() / 2));
    assertThat(list.get(0).getHexDump(), is(notNullValue()));
    assertThat(list.get(0).getCategory(), is("c-apdu"));

    assertThat(list.get(1), is(decodedAs("R-APDU", "9000")));
    assertThat(list.get(1), hasBounds(line1.length() / 2, line1.length() / 2 + line2.length() / 2));
    assertThat(list.get(1).getHexDump(), is(notNullValue()));
    assertThat(list.get(1).getCategory(), is("r-apdu"));
  }

  @Test
  public void testGetMaximumLength() {
    assertThat(decoder.getMaxLength(), is(Integer.MAX_VALUE));
  }
}
