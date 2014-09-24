package io.github.binaryfoo.decoders;

import io.github.binaryfoo.DecodedData;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class AmexTerminalCapabilitiesDecoderTest {

    private AmexTerminalCapabilitiesDecoder decoder = new AmexTerminalCapabilitiesDecoder();

    @Test
    public void testDecodeXp1_0() throws Exception {
        int startIndex = 4;
        List<DecodedData> dataList = decoder.decode("0000", startIndex, null);
        assertThat(dataList, hasItem(new DecodedData("", "Expresspay 1.0", startIndex, startIndex + 2)));
    }

    @Test
    public void testDecodeXp2_0_Magstripe() throws Exception {
        int startIndex = 4;
        List<DecodedData> dataList = decoder.decode("4000", startIndex, null);
        assertThat(dataList, hasItem(new DecodedData("", "Expresspay 2.0 MSD", startIndex, startIndex + 2)));
    }

    @Test
    public void testDecodeXp2_0_EMV() throws Exception {
        int startIndex = 4;
        List<DecodedData> dataList = decoder.decode("8000", startIndex, null);
        assertThat(dataList, hasItem(new DecodedData("", "Expresspay 2.0 EMV & MSD", startIndex, startIndex + 2)));
    }

    @Test
    public void testDecodeXpMobile_EMV() throws Exception {
        int startIndex = 4;
        List<DecodedData> dataList = decoder.decode("C800", startIndex, null);
        assertThat(dataList, hasItem(new DecodedData("", "Expresspay Mobile - EMV", startIndex, startIndex + 2)));
    }

    @Test
    public void testCVMRequired() throws Exception {
        int startIndex = 4;
        List<DecodedData> dataList = decoder.decode("0800", startIndex, null);
        assertThat(dataList, hasItem(new DecodedData("", "CVM Required", startIndex, startIndex + 2)));
    }

    @Test
    public void testCVMNotRequired() throws Exception {
        int startIndex = 4;
        List<DecodedData> dataList = decoder.decode("0000", startIndex, null);
        assertThat(dataList, hasItem(new DecodedData("", "CVM Not Required", startIndex, startIndex + 2)));
    }
}