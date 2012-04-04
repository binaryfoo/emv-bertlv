package com.willcurrie.decoders;

import com.willcurrie.DecodedData;
import com.willcurrie.Decoder;
import com.willcurrie.decoders.apdu.*;
import com.willcurrie.hex.HexDumpElement;
import org.easymock.classextension.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class APDUSequenceDecoderTest extends EasyMockSupport {

    private ReplyAPDUDecoder replyAPDUDecoder;
    private SelectCommandAPDUDecoder selectCommandAPDUDecoder;
    private GetProcessingOptionsCommandAPDUDecoder getProcessingOptionsCommandAPDUDecoder;
    private DecodedData decodedCommand;
    private DecodedData decodedReply;
    private Decoder decoder;

    @Before
    public void setUp() throws Exception {
        selectCommandAPDUDecoder = createMock("selectCommandAPDUDecoder", SelectCommandAPDUDecoder.class);
        replyAPDUDecoder = createMock("replyAPDUDecoder", ReplyAPDUDecoder.class);
        getProcessingOptionsCommandAPDUDecoder = createMock("getProcessingOptionsCommandAPDUDecoder", GetProcessingOptionsCommandAPDUDecoder.class);
        expect(selectCommandAPDUDecoder.getCommand()).andStubReturn(APDUCommand.Select);
        expect(getProcessingOptionsCommandAPDUDecoder.getCommand()).andStubReturn(APDUCommand.GetProcessingOptions);
        decoder = new APDUSequenceDecoder(replyAPDUDecoder, selectCommandAPDUDecoder, getProcessingOptionsCommandAPDUDecoder);
        decodedCommand = createMock(DecodedData.class);
        decodedReply = createMock(DecodedData.class);
    }

    @Test
    public void testOneSelectCommand() throws Exception {
        String input = "00A4040007A000000004101000";
        expect(selectCommandAPDUDecoder.decode(eq(input), eq(0), isA(DecodeSession.class))).andReturn(decodedCommand);
        expect(decodedCommand.getEndIndex()).andReturn(input.length()/2);
        decodedCommand.setHexDump(isA(List.class));
        replayAll();
        List<DecodedData> list = decoder.decode(input, 0, new DecodeSession());
        verifyAll();
        assertThat(list.size(), is(1));
        assertThat(list.get(0), is(decodedCommand));
    }

    @Test
    public void testOneGetProcessingOptionsCommand() throws Exception {
        String input = "80A8000002830000";
        expect(getProcessingOptionsCommandAPDUDecoder.decode(eq(input), eq(0), isA(DecodeSession.class))).andReturn(decodedCommand);
        expect(decodedCommand.getEndIndex()).andReturn(input.length()/2);
        decodedCommand.setHexDump(isA(List.class));
        replayAll();
        List<DecodedData> list = decoder.decode(input, 0, new DecodeSession());
        verifyAll();
        assertThat(list.size(), is(1));
        assertThat(list.get(0), is(decodedCommand));
    }

    @Test
    public void testOneSelectCommandPlusResponse() throws Exception {
        String line1 = "00A4040007A000000004101000";
        String line2 = "6F1C8407A0000000041010A511500F505043204D434420303420207632309000";
        String input = line1 + " " + line2;
        expect(selectCommandAPDUDecoder.decode(eq(line1), eq(0), isA(DecodeSession.class))).andReturn(decodedCommand);
        expect(decodedCommand.getEndIndex()).andReturn(line1.length()/2);
        decodedCommand.setHexDump(isA(List.class));
        expect(replyAPDUDecoder.decode(eq(line2), eq(line1.length()/2), isA(DecodeSession.class))).andReturn(decodedReply);
        expect(decodedReply.getEndIndex()).andReturn(line1.length()/2 + line2.length()/2);
        decodedReply.setHexDump(isA(List.class));
        replayAll();
        List<DecodedData> list = decoder.decode(input, 0, new DecodeSession());
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
