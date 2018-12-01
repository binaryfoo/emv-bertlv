package io.github.binaryfoo

import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import java.nio.file.Files

class RegressionTest {

  @Test
  fun contactMastercard(): Unit = execute("contact-mastercard")

  @Test
  fun contactVisa(): Unit = execute("contact-visa")

  @Test
  fun contactlessMastercard(): Unit = execute("contactless-mastercard")

  @Test
  fun contactlessMsd(): Unit = execute("contactless-msd", "MSD")

  @Test
  fun contactAmexCda(): Unit = execute("amex-cda", "Amex")

  @Test
  fun contactUpi(): Unit = execute("contact-upi", "UPI")

  private fun execute(name: String, meta: String = "EMV") {
    val (input, expectedOutput) = loadCase(name)
    assertEquals(expectedOutput, decode(input, meta).toSimpleString(""))
  }

  private fun loadCase(name: String): Pair<String, String> {
    val input = String(Files.readAllBytes(File("src/test/resources/cases/$name-in.txt").toPath()))
    val expectedOutput = String(Files.readAllBytes(File("src/test/resources/cases/$name-out.txt").toPath()))
    return Pair(input, expectedOutput)
  }

  fun decode(input: String, meta: String): List<DecodedData> {
    return RootDecoder().decode(input, meta, "apdu-sequence")
  }
}

