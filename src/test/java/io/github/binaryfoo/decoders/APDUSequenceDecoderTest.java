package io.github.binaryfoo.decoders;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.Decoder;
import io.github.binaryfoo.decoders.apdu.*;
import org.easymock.classextension.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class APDUSequenceDecoderTest extends EasyMockSupport {

    private ReplyAPDUDecoder replyAPDUDecoder;
    private SelectCommandAPDUDecoder selectCommandAPDUDecoder;
    private GetProcessingOptionsCommandAPDUDecoder getProcessingOptionsCommandAPDUDecoder;
    private Decoder decoder;

    @Before
    public void setUp() throws Exception {
        selectCommandAPDUDecoder = createMock("selectCommandAPDUDecoder", SelectCommandAPDUDecoder.class);
        replyAPDUDecoder = createMock("replyAPDUDecoder", ReplyAPDUDecoder.class);
        getProcessingOptionsCommandAPDUDecoder = createMock("getProcessingOptionsCommandAPDUDecoder", GetProcessingOptionsCommandAPDUDecoder.class);
        expect(selectCommandAPDUDecoder.getCommand()).andStubReturn(APDUCommand.Select);
        expect(getProcessingOptionsCommandAPDUDecoder.getCommand()).andStubReturn(APDUCommand.GetProcessingOptions);
        decoder = new APDUSequenceDecoder(replyAPDUDecoder, selectCommandAPDUDecoder, getProcessingOptionsCommandAPDUDecoder);
    }

    @Test
    public void testOneSelectCommand() throws Exception {
        String input = "00A4040007A000000004101000";
        DecodedData decodedCommand = DecodedData.primitive("", "", 0, input.length() / 2);
        expect(selectCommandAPDUDecoder.decode(eq(input), eq(0), isA(DecodeSession.class))).andReturn(decodedCommand);

        replayAll();
        List<DecodedData> list = decoder.decode(input, 0, new DecodeSession());
        verifyAll();
        assertThat(list.size(), is(1));
        assertThat(list.get(0), is(decodedCommand));
        assertThat(decodedCommand.getHexDump(), is(notNullValue()));
    }

    @Test
    public void testOneGetProcessingOptionsCommand() throws Exception {
        String input = "80A8000002830000";
        DecodedData decodedCommand = DecodedData.primitive("", "", 0, input.length() / 2);
        expect(getProcessingOptionsCommandAPDUDecoder.decode(eq(input), eq(0), isA(DecodeSession.class))).andReturn(decodedCommand);

        replayAll();
        List<DecodedData> list = decoder.decode(input, 0, new DecodeSession());
        verifyAll();
        assertThat(list.size(), is(1));
        assertThat(list.get(0), is(decodedCommand));
        assertThat(decodedCommand.getHexDump(), is(notNullValue()));
    }

    @Test
    public void testOneSelectCommandPlusResponse() throws Exception {
        String line1 = "00A4040007A000000004101000";
        String line2 = "6F1C8407A0000000041010A511500F505043204D434420303420207632309000";
        String input = line1 + " " + line2;

        DecodedData decodedCommand = DecodedData.primitive("", "", 0, line1.length() / 2);
        DecodedData decodedReply = DecodedData.primitive("", "", 0, line1.length()/2 + line2.length()/2);

        expect(selectCommandAPDUDecoder.decode(eq(line1), eq(0), isA(DecodeSession.class))).andReturn(decodedCommand);
        expect(replyAPDUDecoder.decode(eq(line2), eq(line1.length()/2), isA(DecodeSession.class))).andReturn(decodedReply);

        replayAll();
        List<DecodedData> list = decoder.decode(input, 0, new DecodeSession());
        verifyAll();

        assertThat(list.size(), is(2));
        assertThat(list.get(0), is(decodedCommand));
        assertThat(decodedCommand.getHexDump(), is(notNullValue()));
        assertThat(list.get(1), is(decodedReply));
        assertThat(decodedReply.getHexDump(), is(notNullValue()));
    }

    @Test
    public void testGetMaximumLength() throws Exception {
        assertThat(decoder.getMaxLength(), is(Integer.MAX_VALUE));
    }
}
