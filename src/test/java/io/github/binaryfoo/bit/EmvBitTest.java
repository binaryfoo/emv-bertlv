package io.github.binaryfoo.bit;

import org.junit.Test;

import java.util.*;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class EmvBitTest {

    @Test
    public void decodeBits() throws Exception {
        Set<EmvBit> decoded = EmvBit.fromHex("0080");
        assertThat(decoded.size(), is(16));
        assertThat(decoded, hasItem(new EmvBit(1, 8, false)));
        assertThat(decoded, hasItem(new EmvBit(2, 8, true)));
        assertThat(decoded, hasItem(new EmvBit(2, 1, false)));
    }

    @Test
    public void decodeAllOnes() throws Exception {
        List<EmvBit> decoded = new ArrayList<>(EmvBit.fromHex("ffff"));
        assertThat(decoded.size(), is(16));
        for (int i = 0; i < 16; i++) {
            assertThat(decoded.get(i), is(new EmvBit(i/8 + 1, 8 - i%8, true)));
        }
    }

    @Test
    public void decodeAllZeroes() throws Exception {
        List<EmvBit> decoded = new ArrayList<>(EmvBit.fromHex("0000"));
        assertThat(decoded.size(), is(16));
        for (int i = 0; i < 16; i++) {
            assertThat(decoded.get(i), is(new EmvBit(i/8 + 1, 8 - i%8, false)));
        }
    }
}