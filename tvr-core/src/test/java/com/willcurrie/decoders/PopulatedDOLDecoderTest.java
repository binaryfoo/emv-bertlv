package com.willcurrie.decoders;

import com.willcurrie.DecodedData;
import com.willcurrie.QVsdcTags;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PopulatedDOLDecoderTest {
    @Test
    public void testDecode() throws Exception {
        DecodeSession session = new DecodeSession();
        session.setTagMetaData(QVsdcTags.METADATA);
        List<DecodedData> decoded = new PopulatedDOLDecoder().decode("9F66049F02069F03069F1A0295055F2A029A039C019F3704:36000000000000001000000000000000003600000000000036120315000008E4C8", 0, session);
        assertThat(decoded.size(), is(9));
        assertThat(decoded.get(0).getRawData(), is("9F66 (TTQ)"));
        assertThat(decoded.get(0).getDecodedData(), is("36000000"));
        assertThat(decoded.get(0).getStartIndex(), is(24));
        assertThat(decoded.get(0).getEndIndex(), is(28));
        assertThat(decoded.get(8).getRawData(), is("9F37 (unpredictable number)"));
        assertThat(decoded.get(8).getDecodedData(), is("0008E4C8"));
        assertThat(decoded.get(8).getStartIndex(), is(53));
        assertThat(decoded.get(8).getEndIndex(), is(57));
    }

    @Test
    public void testDecodeTwoParameters() throws Exception {
        DecodeSession session = new DecodeSession();
        session.setTagMetaData(QVsdcTags.METADATA);
        List<DecodedData> decoded = new PopulatedDOLDecoder().decode("9F66049F02069F03069F1A0295055F2A029A039C019F3704", "36000000000000001000000000000000003600000000000036120315000008E4C8", 0, session);
        assertThat(decoded.size(), is(9));
        assertThat(decoded.get(0).getRawData(), is("9F66 (TTQ)"));
        assertThat(decoded.get(0).getDecodedData(), is("36000000"));
        List<DecodedData> expectedDecodedTTQ = QVsdcTags.METADATA.get(QVsdcTags.TERMINAL_TX_QUALIFIERS).getDecoder().decode("36000000", 0, new DecodeSession());
        assertThat(decoded.get(0).getChildren(), is(expectedDecodedTTQ));
        assertThat(decoded.get(0).getStartIndex(), is(0));
        assertThat(decoded.get(0).getEndIndex(), is(4));
        assertThat(decoded.get(8).getRawData(), is("9F37 (unpredictable number)"));
        assertThat(decoded.get(8).getDecodedData(), is("0008E4C8"));
        assertThat(decoded.get(8).getStartIndex(), is(29));
        assertThat(decoded.get(8).getEndIndex(), is(33));
    }

    @Test
    public void testDecodeIncludesPrimitiveElementsInPopulatedList() throws Exception {
        DecodeSession session = new DecodeSession();
        session.setTagMetaData(QVsdcTags.METADATA);
        int startIndex = 12;
        List<DecodedData> decoded = new PopulatedDOLDecoder().decode("8A029F02069F03069F1A0295055F2A029A039C019F3704", "303000000000100000000000000000368000008040003612031600D3173A1F", startIndex, session);
        assertThat(decoded.size(), is(9));
        assertThat(decoded.get(0).getRawData(), is("8A (authorisation response code)"));
        assertThat(decoded.get(0).getDecodedData(), is("00"));
        assertThat(decoded.get(0).getStartIndex(), is(startIndex));
        assertThat(decoded.get(0).getEndIndex(), is(startIndex+2));
    }
}
