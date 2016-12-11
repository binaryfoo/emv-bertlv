package io.github.binaryfoo.decoders.bit

import io.github.binaryfoo.bit.*
import io.github.binaryfoo.bit.EmvBit
import org.junit.Test

import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.nullValue
import org.junit.Assert.assertThat

class EnumeratedBitStringFieldTest {

    @Test fun positionInProvidesOptionalHexString() {
        val field = EnumeratedBitStringField(setOf(EmvBit(3, 8, true)), "V1")
        assertThat(field.getPositionIn(null), `is`("Byte 3 Bit 8 = 1"))
        assertThat(field.getPositionIn(fromHex("000000")), `is`("000080 (Byte 3 Bit 8)"))
    }

    @Test fun hexStringInPositionIncludesValuesForTwoBitField() {
        val field = EnumeratedBitStringField(setOf(EmvBit(2, 8, true), EmvBit(2, 6, true)), "V1")
        assertThat(field.getPositionIn(fromHex("000000")), `is`("00A000 (Byte 2 Bit 8 = 1, Byte 2 Bit 6 = 1)"))
    }

    @Test fun twoBits() {
        val field = EnumeratedBitStringField(setOf(EmvBit(3, 8, true), EmvBit(3, 7, false)), "V1")
        assertThat<String>(field.getValueIn(fromHex("000080")), `is`("V1"))
        assertThat<String>(field.getValueIn(fromHex("0000C0")), `is`(nullValue()))
        assertThat(field.getPositionIn(null), `is`("Byte 3 Bit 8 = 1, Byte 3 Bit 7 = 0"))
        assertThat(field.getStartBytesOffset(), `is`(2))
        assertThat(field.getLengthInBytes(), `is`(1))
    }
}