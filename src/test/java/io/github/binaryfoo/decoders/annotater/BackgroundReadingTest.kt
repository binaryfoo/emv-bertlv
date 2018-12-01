package io.github.binaryfoo.decoders.annotater

import io.github.binaryfoo.decoders.annotator.BackgroundReading
import io.github.binaryfoo.decoders.apdu.APDUCommand
import org.hamcrest.Matchers.containsString
import org.junit.Assert.assertThat
import org.junit.Test

class BackgroundReadingTest {

  @Test
  fun load() {
    assertThat(BackgroundReading.readingFor(APDUCommand.ReadRecord)?.get("short"), containsString("pair (SFI, record number)"))
    assertThat(BackgroundReading.readingFor(APDUCommand.ReadRecord)?.get("long"), containsString("short file indicator"))
  }
}
