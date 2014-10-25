package io.github.binaryfoo.decoders.apdu;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.decoders.DecodeSession;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ReadRecordAPDUDecoderTest {

    @Test
    public void testDecode() throws Exception {
        DecodedData decoded = new ReadRecordAPDUDecoder().decode("00B2011400", 0, new DecodeSession());
        assertThat(decoded.getRawData(), is("C-APDU: Read Record"));
        assertThat(decoded.getDecodedData(), is("SFI 2 record 1"));
    }
}
