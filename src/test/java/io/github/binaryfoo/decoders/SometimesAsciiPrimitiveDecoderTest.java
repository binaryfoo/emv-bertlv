package io.github.binaryfoo.decoders;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class SometimesAsciiPrimitiveDecoderTest {

    @Test
    public void decodesAscii() throws Exception {
        String decoded = new SometimesAsciiPrimitiveDecoder().decode("315041592E5359532E4444463031");
        assertThat(decoded, is("1PAY.SYS.DDF01"));
    }

    @Test
    public void puntsToHexWhenNotAscii() throws Exception {
        String decoded = new SometimesAsciiPrimitiveDecoder().decode("A000000025010801");
        assertThat(decoded, is("A000000025010801"));
    }
}