package com.willcurrie.decoders;

import com.willcurrie.DecodedData;
import com.willcurrie.Decoder;
import org.easymock.classextension.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.easymock.EasyMock.expect;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class APDUSequenceDecoderTest extends EasyMockSupport {

    private ReplyAPDUDecoder replyAPDUDecoder;
    private SelectCommandAPDUDecoder selectCommandAPDUDecoder;
    private DecodedData decodedCommand;
    private DecodedData decodedReply;
    private Decoder decoder;

    @Before
    public void setUp() throws Exception {
        replyAPDUDecoder = createMock(ReplyAPDUDecoder.class);
        selectCommandAPDUDecoder = createMock(SelectCommandAPDUDecoder.class);
        expect(selectCommandAPDUDecoder.getCommand()).andStubReturn(APDUCommand.Select);
        decoder = new APDUSequenceDecoder(replyAPDUDecoder, selectCommandAPDUDecoder);
        decodedCommand = createMock(DecodedData.class);
        decodedReply = createMock(DecodedData.class);
    }

    @Test
    public void testOneSelectCommand() throws Exception {
        String input = "00A4040007A000000004101000";
        expect(selectCommandAPDUDecoder.decode(input, 0)).andReturn(decodedCommand);
        expect(decodedCommand.getEndIndex()).andReturn(input.length());
        replayAll();
        List<DecodedData> list = decoder.decode(input, 0);
        verifyAll();
        assertThat(list.size(), is(1));
        assertThat(list.get(0), is(decodedCommand));
    }

    @Test
    public void testOneSelectCommandPlusResponse() throws Exception {
        String line1 = "00A4040007A000000004101000";
        String line2 = "6F1C8407A0000000041010A511500F505043204D434420303420207632309000";
        String input = line1 + line2;
        expect(selectCommandAPDUDecoder.decode(input, 0)).andReturn(decodedCommand);
        expect(decodedCommand.getEndIndex()).andReturn(line1.length());
        expect(replyAPDUDecoder.decode(input, line1.length())).andReturn(decodedReply);
        expect(decodedReply.getEndIndex()).andReturn(line1.length() + line2.length());
        replayAll();
        List<DecodedData> list = decoder.decode(input, 0);
        verifyAll();
        assertThat(list.size(), is(2));
        assertThat(list.get(0), is(decodedCommand));
        assertThat(list.get(1), is(decodedReply));
    }

    @Test
    public void testGetMaximumLength() throws Exception {
        assertThat(decoder.getMaxLength(), is(Integer.MAX_VALUE));
    }
}
