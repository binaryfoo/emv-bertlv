package com.willcurrie.decoders.apdu;

import com.willcurrie.DecodedData;
import com.willcurrie.EmvTags;
import com.willcurrie.decoders.DecodeSession;
import org.junit.Test;

import static org.hamcrest.collection.IsCollectionContaining.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class GenerateACAPDUDecoderTest {
    @Test
    public void testDecode() throws Exception {
        String input = "80AE50002B00000001000100000000000000020000000000003612031500000EEC8522000000000000000000005E030000";
        DecodeSession session = new DecodeSession();
        session.put(EmvTags.CDOL_1, "9F02069F03069F090295055F2A029A039C019F37049F35019F45029F4C089F3403");
        int startIndex = 6;
        DecodedData decoded = new GenerateACAPDUDecoder().decode(input, startIndex, session);
        assertThat(decoded.getRawData(), is("C-APDU: Generate AC (TC+CDA)"));
        assertThat(decoded.getChildren(), hasItem(new DecodedData(EmvTags.AMOUNT_AUTHORIZED, "000000010001", startIndex + 5, startIndex + 11)));
        assertThat(decoded.getChildren(), hasItem(new DecodedData(EmvTags.CVM_RESULTS, "5E0300", startIndex + 45, startIndex + 48)));
    }
}
