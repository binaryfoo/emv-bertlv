package com.willcurrie.decoders;

import com.willcurrie.DecodedData;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class PopulatedDOLDecoderTest {
    @Test
    public void testDecode() throws Exception {
        List<DecodedData> decoded = new PopulatedDOLDecoder().decode("9F66049F02069F03069F1A0295055F2A029A039C019F3704:832136000000000000001000000000000000003600000000000036120315000008E4C8", 0);
        assertThat(decoded.size(), is(9));
        assertThat(decoded.get(0).getRawData(), is("9F66 (terminal transaction qualifiers)"));
        assertThat(decoded.get(0).getDecodedData(), is("36000000"));
        assertThat(decoded.get(0).getStartIndex(), is(26));
        assertThat(decoded.get(0).getEndIndex(), is(30));
        assertThat(decoded.get(8).getRawData(), is("9F37 (unpredictable number)"));
        assertThat(decoded.get(8).getDecodedData(), is("0008E4C8"));
        assertThat(decoded.get(8).getStartIndex(), is(55));
        assertThat(decoded.get(8).getEndIndex(), is(59));
    }
}
