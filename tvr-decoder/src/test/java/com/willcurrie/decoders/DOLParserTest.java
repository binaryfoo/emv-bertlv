package com.willcurrie.decoders;

import com.willcurrie.tlv.ISOUtil;
import com.willcurrie.tlv.Tag;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DOLParserTest {

    @Test
    public void testParse() throws Exception {
        List<DOLParser.DOLElement> elements = new DOLParser().parse(ISOUtil.hex2byte("9F66049F02069F03069F1A0295055F2A029A039C019F3704"));
        assertThat(elements.size(), is(9));
        assertThat(elements.get(0), is(new DOLParser.DOLElement(Tag.fromHex("9F66"), 4)));
        assertThat(elements.get(8), is(new DOLParser.DOLElement(Tag.fromHex("9F37"), 4)));
    }
}
