package com.willcurrie.decoders.apdu;

import com.willcurrie.DecodedData;
import com.willcurrie.decoders.DecodeSession;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ReadRecordAPDUDecoderTest {

    @Test
    public void testDecode() throws Exception {
        DecodedData decoded = new ReadRecordAPDUDecoder().decode("00B2011400", 0, new DecodeSession());
        assertThat(decoded.getRawData(), is("C-APDU: Read Record"));
        assertThat(decoded.getDecodedData(), is("number 01 SFI 2"));
    }
}
