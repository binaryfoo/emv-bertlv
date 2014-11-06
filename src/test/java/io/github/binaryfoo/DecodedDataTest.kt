package io.github.binaryfoo

import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test
import io.github.binaryfoo.tlv.BerTlv
import io.github.binaryfoo.tlv.Tag
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.hamcrest.MatcherAssert
import kotlin.test.assertEquals
import kotlin.test.assertNull

public class DecodedDataTest {

    Test
    public fun hexDumpReferenceIncludesTlvTagAndLengthPosition() {
        val decoded = DecodedData.fromTlv(BerTlv.newInstance(Tag.fromHex("9F1B"), "112233"), EmvTags.METADATA, "", 42, 48)
        assertEquals(42..47, decoded.positionInHexDump)
        assertEquals(42..43, decoded.tagPositionInHexDump)
        assertEquals(44..44, decoded.lengthPositionInHexDump)
    }

    Test
    public fun hexDumpReferenceWithTagOnlyHasNoTagOrLengthPosition() {
        val decoded = DecodedData.withTag(Tag.fromHex("9F1B"), EmvTags.METADATA, "112233", 42, 45)
        assertEquals(42..44, decoded.positionInHexDump)
        assertEquals(null, decoded.tagPositionInHexDump)
        assertEquals(null, decoded.lengthPositionInHexDump)
    }

    Test
    public fun hexDumpReferenceWithNoTagOrTlv() {
        val decoded = DecodedData.primitive("332211", "", 42, 45)
        assertEquals(42..44, decoded.positionInHexDump)
        assertEquals(null, decoded.tagPositionInHexDump)
        assertEquals(null, decoded.lengthPositionInHexDump)
    }
}

