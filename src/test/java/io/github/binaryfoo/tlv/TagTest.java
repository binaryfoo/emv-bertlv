package io.github.binaryfoo.tlv;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class TagTest {

  private static final byte[] TAG_E1 = {(byte) 0xE1};
  private static final byte[] TAG_1F = {(byte) 0x1F};
  private static final byte[] TAG_3F = {(byte) 0x3F};
  private static final byte[] TAG_NEGATIVE_1 = {(byte) -1};
  private static final byte[] TAG_9F39 = {(byte) 0x9f, (byte) 0x39};
  private static final byte[] TAG_9F1A = {(byte) 0x9f, (byte) 0x1A};
  private static final byte[] TAG_9FC8 = {(byte) 0x9f, (byte) 0xC8};
  private static final byte[] TAG_9FC81A = {(byte) 0x9f, (byte) 0xC8, (byte) 0x1A};
  private static final byte[] TAG_9F1A1A = {(byte) 0x9f, (byte) 0x1A, (byte) 0x1A};
  private static final byte[] TAG_9FC8C8 = {(byte) 0x9f, (byte) 0xC8, (byte) 0xC8};

  @Test
  public void testHexStringFactoryMethod() {
    Tag tag = Tag.fromHex("9FC81A");
    assertArrayEquals(TAG_9FC81A, tag.getByteArray());
  }

  @Test
  public void testConstructTagWithNull() {
    doIllegalArgumentTest(null);
  }

  @Test
  public void testConstructTagWithEmptyByteArray() {
    doIllegalArgumentTest(new byte[]{});
  }

  @Test
  public void testConstructTagWithNegative1() {
    doIllegalArgumentTest(TAG_NEGATIVE_1);
  }

  @Test
  public void testConstructTagWith1F() {
    doIllegalArgumentTest(TAG_1F);
  }

  @Test
  public void testConstructTagWith3F() {
    doIllegalArgumentTest(TAG_3F);
  }

  @Test
  public void testConstructTagWithE1() {
    doValidTest(TAG_E1);
  }

  @Test
  public void testConstructTagWith9F39() {
    doValidTest(TAG_9F39);
  }

  @Test
  public void testConstructTagWith9F1A() {
    doValidTest(TAG_9F1A);
  }

  @Test
  public void testConstructTagWith9FC8() {
    doIllegalArgumentTest(TAG_9FC8);
  }

  @Test
  public void testConstructTagWith9FC81A() {
    doValidTest(TAG_9FC81A);
  }

  @Test
  public void testConstructTagWith9F1A1A() {
    doIllegalArgumentTest(TAG_9F1A1A);
  }

  @Test
  public void testConstructTagWith9FC8C8() {
    doIllegalArgumentTest(TAG_9FC8C8);
  }

  private void doValidTest(byte[] bytes) {
    Tag tag = new Tag(bytes, true);
    assertArrayEquals(bytes, tag.getByteArray());
  }

  private void doIllegalArgumentTest(byte[] bytes) {
    try {
      new Tag(bytes, true);
      fail();
    } catch (IllegalArgumentException ignored) {
    }
  }

  @Test
  public void testIsConstructed() {
    assertTrue(Tag.fromHex("21").isConstructed());
    assertFalse(Tag.fromHex("01").isConstructed());
    assertFalse(Tag.fromHex("8F").isConstructed());
  }

  @Test
  public void testParse() {
    doTestParse(TAG_E1);
    doTestParse(TAG_9F39);
    doTestParse(TAG_9F1A);
    doTestParse(TAG_9FC81A);
  }

  @Test
  public void testParseListOfTags() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(20);
    byteBuffer.put(TAG_E1);
    byteBuffer.put(TAG_9F39);
    byteBuffer.put(TAG_9F1A);
    byteBuffer.put(TAG_9FC81A);
    int endPosition = byteBuffer.position();
    byteBuffer.flip();
    BerTlvUtils.assertEquals(TAG_E1, Tag.parse(byteBuffer).getByteArray());
    BerTlvUtils.assertEquals(TAG_9F39, Tag.parse(byteBuffer).getByteArray());
    BerTlvUtils.assertEquals(TAG_9F1A, Tag.parse(byteBuffer).getByteArray());
    BerTlvUtils.assertEquals(TAG_9FC81A, Tag.parse(byteBuffer).getByteArray());
    assertEquals(endPosition, byteBuffer.position());
  }

  @Test
  public void nonStandard9F84() {
    Tag parsedTag = Tag.parse(asBuffer("9F84"), new QuirkListTagMode(Collections.singleton("9F84")));
    assertThat(parsedTag.getHexString(), is("9F84"));
  }

  @Test
  public void commonVendorErrorMode() {
    assertThat(Tag.parse(asBuffer("9F81"), CommonVendorErrorMode.INSTANCE).getHexString(), is("9F81"));
    assertThat(Tag.parse(asBuffer("9F8A"), CommonVendorErrorMode.INSTANCE).getHexString(), is("9F8A"));
    assertThat(Tag.parse(asBuffer("9F8F"), CommonVendorErrorMode.INSTANCE).getHexString(), is("9F8F"));
    assertThat(Tag.parse(asBuffer("9FC81A"), CommonVendorErrorMode.INSTANCE).getHexString(), is("9FC81A"));
  }

  private void doTestParse(byte[] rawTag) {
    ByteBuffer buffer = asBuffer(rawTag);
    Tag parsedTag = Tag.parse(buffer);
    BerTlvUtils.assertEquals(rawTag, parsedTag.getByteArray());
    assertEquals(rawTag.length, buffer.position());
  }

  private ByteBuffer asBuffer(String tagAsHex) {
    return asBuffer(ISOUtil.hex2byte(tagAsHex));
  }

  private ByteBuffer asBuffer(byte[] rawTag) {
    ByteBuffer buffer = ByteBuffer.allocate(8);
    buffer.put(rawTag);
    return (ByteBuffer) buffer.flip();
  }

}
