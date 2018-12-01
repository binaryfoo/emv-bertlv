package io.github.binaryfoo.tlv;

import org.junit.Assert;

public final class BerTlvUtils {

  private BerTlvUtils() {
  }

  public static void assertEquals(byte[] expected, byte[] actual) {
    Assert.assertEquals(ISOUtil.hexString(expected), ISOUtil.hexString(actual));
  }

  public static void assertEquals(String expected, byte[] actual) {
    Assert.assertEquals(expected, ISOUtil.hexString(actual));
  }
}

