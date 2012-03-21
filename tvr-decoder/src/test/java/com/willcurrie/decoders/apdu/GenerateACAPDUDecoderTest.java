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
        session.setTagMetaData(EmvTags.METADATA);
        session.put(EmvTags.CDOL_1, "9F02069F03069F090295055F2A029A039C019F37049F35019F45029F4C089F3403");
        int startIndex = 6;
        DecodedData decoded = new GenerateACAPDUDecoder().decode(input, startIndex, session);
        assertThat(decoded.getRawData(), is("C-APDU: Generate AC (TC+CDA)"));
        assertThat(decoded.getChildren(), hasItem(new DecodedData(EmvTags.AMOUNT_AUTHORIZED.toString(EmvTags.METADATA), "000000010001", startIndex + 5, startIndex + 11)));
        assertThat(decoded.getChildren(), hasItem(new DecodedData(EmvTags.CVM_RESULTS.toString(EmvTags.METADATA), "5E0300", startIndex + 45, startIndex + 48)));
    }

    @Test
    public void testDecodeSecondGenerateAC() throws Exception {
        String first = "80AE80002B000000001000000000000000000280000080000036120316008221F60122000000000000000000005E030000";
        String second = "80AE40001D11223344556677880000303080000080008221F601000000000000000000";
        DecodeSession session = new DecodeSession();
        session.setTagMetaData(EmvTags.METADATA);
        session.put(EmvTags.CDOL_1, "9F02069F03069F090295055F2A029A039C019F37049F35019F45029F4C089F3403");
        session.put(EmvTags.CDOL_2, "910A8A0295059F37049F4C08");

        DecodedData firstDecoded = new GenerateACAPDUDecoder().decode(first, 0, session);
        assertThat(firstDecoded.getRawData(), is("C-APDU: Generate AC (ARQC)"));

        DecodedData secondDecoded = new GenerateACAPDUDecoder().decode(second, 0, session);
        assertThat(secondDecoded.getRawData(), is("C-APDU: Generate AC (TC)"));
        assertThat(secondDecoded.getChildren(), hasItem(new DecodedData(EmvTags.ISSUER_AUTHENTICATION_DATA.toString(EmvTags.METADATA), "11223344556677880000", 5, 15)));
        assertThat(secondDecoded.getChildren(), hasItem(new DecodedData(EmvTags.ICC_DYNAMIC_NUMBER.toString(EmvTags.METADATA), "0000000000000000", 26, 34)));
    }
}
