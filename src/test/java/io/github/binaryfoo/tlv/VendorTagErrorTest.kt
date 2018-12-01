package io.github.binaryfoo.tlv

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Assert
import org.junit.Test
import java.util.*

class VendorTagErrorTest {

  @Test
  fun commonTagError() {
    assertThat(CommonVendorErrorMode.isCommonError("91".decodeAsHex()), `is`(false))
    assertThat(CommonVendorErrorMode.isCommonError("9F89".decodeAsHex()), `is`(true))
    assertThat(CommonVendorErrorMode.isCommonError("9F8901".decodeAsHex()), `is`(true))
    assertThat(CommonVendorErrorMode.isCommonError("9FC101".decodeAsHex()), `is`(false))
  }
}

