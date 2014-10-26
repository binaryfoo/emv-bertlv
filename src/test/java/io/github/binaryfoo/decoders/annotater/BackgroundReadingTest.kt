package io.github.binaryfoo.decoders.annotater

import io.github.binaryfoo.decoders.annotator.BackgroundReading
import io.github.binaryfoo.decoders.apdu.APDUCommand
import org.junit.Test

import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.Assert.assertThat

public class BackgroundReadingTest {

    Test
    public fun load() {
        assertThat(BackgroundReading.readingFor(APDUCommand.ReadRecord)?.get("short"), containsString("pair (SFI, record number)"))
        assertThat(BackgroundReading.readingFor(APDUCommand.ReadRecord)?.get("long"), containsString("short file indicator"))
    }
}
