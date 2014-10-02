package io.github.binaryfoo.decoders.apdu;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class ResponseCodeTest {

    @Test
    public void decode() throws Exception {
        assertThat(ResponseCode.lookup("9000").getDescription(), is("OK"));
    }
}