package io.github.binaryfoo.decoders;

import io.github.binaryfoo.DecodedData;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ApplicationFileLocatorDecoderTest {
    @Test
    public void testDecode() throws Exception {
        int startIndexInBytes = 3;
        List<DecodedData> decoded = new ApplicationFileLocatorDecoder().decode("100102001801010118040400", startIndexInBytes, new DecodeSession());
        assertThat(decoded.get(0), is(DecodedData.primitive("", "SFI 2 records 1-2", startIndexInBytes, startIndexInBytes + 4)));
        assertThat(decoded.get(1), is(DecodedData.primitive("", "SFI 3 record 1", startIndexInBytes + 4, startIndexInBytes + 8)));
        assertThat(decoded.get(2), is(DecodedData.primitive("", "SFI 3 record 4", startIndexInBytes + 8, startIndexInBytes + 12)));
    }
}
