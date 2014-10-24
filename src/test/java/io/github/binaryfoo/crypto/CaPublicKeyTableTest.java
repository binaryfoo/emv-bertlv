package io.github.binaryfoo.crypto;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CaPublicKeyTableTest {

    @Test
    public void loadsKeys() throws Exception {
        CaPublicKey key = CaPublicKeyTable.getEntry("A000000003", "95");
        assertThat(key.getModulus(), is("BE9E1FA5E9A803852999C4AB432DB28600DCD9DAB76DFAAA47355A0FE37B1508AC6BF38860D3C6C2E5B12A3CAAF2A7005A7241EBAA7771112C74CF9A0634652FBCA0E5980C54A64761EA101A114E0F0B5572ADD57D010B7C9C887E104CA4EE1272DA66D997B9A90B5A6D624AB6C57E73C8F919000EB5F684898EF8C3DBEFB330C62660BED88EA78E909AFF05F6DA627B"));
        assertThat(key.getExponent(), is("03"));
    }
}