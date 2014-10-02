package io.github.binaryfoo;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class RootDecoderTest {

    @Test
    public void keepTryingWhenDecodeFailsOnOneAPDU() throws Exception {
        String apdus = "00B2020C00\n" +
                "700E9F080200019F420209789F4A01829000\n" +
                "00AABBCCDDEE\n" +
                "00B2013400\n";
        List<DecodedData> decoded = new RootDecoder().decode(apdus, "EMV", "apdu-sequence");
        assertThat(decoded.size(), is(4));
    }
}