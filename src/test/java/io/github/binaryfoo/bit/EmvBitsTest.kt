package io.github.binaryfoo.bit

import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class EmvBitsTest {

  @Test
  fun bitsToConfigString() {
    val bits = setOf(EmvBit(1, 8, true), EmvBit(1, 1, false))
    assertThat(bits.toConfigString(), `is`("(1,8)=1 & (1,1)=0"))
  }

  @Test
  fun bitToConfigString() {
    assertThat(EmvBit(2, 5, true).toConfigString(), `is`("(2,5)=1"))
  }

  @Test
  fun count() {
    assertThat(fromHex("01").getByteCount(), `is`(1))
    assertThat(fromHex("0000").getByteCount(), `is`(2))
  }

  @Test
  fun match() {
    val byteOneBit4 = fromHex("0800").reduceToOnBits()
    assertThat(byteOneBit4.matches(fromHex("FFFF")), `is`(true))
    assertThat(byteOneBit4.matches(fromHex("8000")), `is`(false))
    assertThat(byteOneBit4.matches(fromHex("08")), `is`(true))
    assertThat(setOf(EmvBit(2, 3, false)).matches(fromHex("FF0B")), `is`(true))
  }

  @Test
  fun setToString() {
    val set = setOf(EmvBit(1, 8, true), EmvBit(1, 1, false))
    assertThat(set.toString(false), `is`("Byte 1 Bit 8, Byte 1 Bit 1"))
    assertThat(set.toString(true), `is`("Byte 1 Bit 8 = 1, Byte 1 Bit 1 = 0"))
  }
}
