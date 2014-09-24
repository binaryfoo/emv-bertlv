package io.github.binaryfoo.decoders;

import io.github.binaryfoo.DecodedData;
import org.junit.Test;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.*;

public class MobileCVMDecoderTest {

    private MobileCVMDecoder decoder = new MobileCVMDecoder();

    @Test
    public void testMobileCVMPerformed() throws Exception {
        assertThat(decoder.decode("010000", 0, null), hasItem(new DecodedData("", "Mobile CVM Performed", 0, 1)));
    }

    @Test
    public void testNoCVMPerformed() throws Exception {
        assertThat(decoder.decode("3F0000", 0, null), hasItem(new DecodedData("", "No CVM Performed", 0, 1)));
    }

    @Test
    public void testCVMCondition_NotRequired() throws Exception {
        assertThat(decoder.decode("000000", 0, null), hasItem(new DecodedData("", "Mobile CVM Not Required", 1, 2)));
    }

    @Test
    public void testCVMCondition_Required() throws Exception {
        assertThat(decoder.decode("000300", 0, null), hasItem(new DecodedData("", "Terminal Required CVM", 1, 2)));
    }

    @Test
    public void testCVMResults_Failed() throws Exception {
        assertThat(decoder.decode("000001", 0, null), hasItem(new DecodedData("", "Mobile CVM Failed", 2, 3)));
    }

    @Test
    public void testCVMResults_Successful() throws Exception {
        assertThat(decoder.decode("000002", 0, null), hasItem(new DecodedData("", "Mobile CVM Successful", 2, 3)));
    }

    @Test
    public void testCVMResults_Blocked() throws Exception {
        assertThat(decoder.decode("000003", 0, null), hasItem(new DecodedData("", "Mobile CVM Blocked", 2, 3)));
    }
}