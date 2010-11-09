package com.willcurrie.tlv;

import java.nio.ByteBuffer;
import java.util.Arrays;

import junit.framework.TestCase;

public class TagTest extends TestCase {

    private static final byte[] TAG_E1 = new byte[]{(byte) 0xE1};
    private static final byte[] TAG_1F = new byte[]{(byte) 0x1F};
    private static final byte[] TAG_3F = new byte[]{(byte) 0x3F};
    private static final byte[] TAG_NEGATIVE_1 = new byte[]{(byte) -1};
    private static final byte[] TAG_9F39 = new byte[]{(byte) 0x9f, (byte) 0x39};
    private static final byte[] TAG_9F1A = new byte[]{(byte) 0x9f, (byte) 0x1A};
    private static final byte[] TAG_9FC8 = new byte[]{(byte) 0x9f, (byte) 0xC8};
    private static final byte[] TAG_9FC81A = new byte[]{(byte) 0x9f, (byte) 0xC8, (byte) 0x1A};
    private static final byte[] TAG_9F1A1A = new byte[]{(byte) 0x9f, (byte) 0x1A, (byte) 0x1A};
    private static final byte[] TAG_9FC8C8 = new byte[]{(byte) 0x9f, (byte) 0xC8, (byte) 0xC8};

    public void testHexStringFactoryMethod() throws Exception {
        Tag tag = Tag.fromHex("9FC81A");
        assertTrue(Arrays.equals(TAG_9FC81A, tag.getBytes()));
    }

    public void testConstructTagWithNull() throws Exception {
        doIllegalArgumentTest(null);
    }

    public void testConstructTagWithEmptyByteArray() throws Exception {
        doIllegalArgumentTest(new byte[]{});
    }

    public void testConstructTagWithNegative1() throws Exception {
        doIllegalArgumentTest(TAG_NEGATIVE_1);
    }

    public void testConstructTagWith1F() throws Exception {
        doIllegalArgumentTest(TAG_1F);
    }

    public void testConstructTagWith3F() throws Exception {
        doIllegalArgumentTest(TAG_3F);
    }

    public void testConstructTagWithE1() throws Exception {
        doValidTest(TAG_E1);
    }

    public void testConstructTagWith9F39() throws Exception {
        doValidTest(TAG_9F39);
    }

    public void testConstructTagWith9F1A() throws Exception {
        doValidTest(TAG_9F1A);
    }

    public void testConstructTagWith9FC8() throws Exception {
        doIllegalArgumentTest(TAG_9FC8);
    }

    public void testConstructTagWith9FC81A() throws Exception {
        doValidTest(TAG_9FC81A);
    }

    public void testConstructTagWith9F1A1A() throws Exception {
        doIllegalArgumentTest(TAG_9F1A1A);
    }

    public void testConstructTagWith9FC8C8() throws Exception {
        doIllegalArgumentTest(TAG_9FC8C8);
    }

    private void doValidTest(byte[] bytes) {
        Tag tag = new Tag(bytes);
        assertTrue(Arrays.equals(bytes, tag.getBytes()));
    }

    private void doIllegalArgumentTest(byte[] bytes) {
        try {
            new Tag(bytes);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    public void testIsConstructed() throws Exception {
        assertTrue(Tag.fromHex("21").isConstructed());
        assertFalse(Tag.fromHex("01").isConstructed());
        assertFalse(Tag.fromHex("8F").isConstructed());
    }

    public void testParse() throws Exception {
        doTestParse(TAG_E1);
        doTestParse(TAG_9F39);
        doTestParse(TAG_9F1A);
        doTestParse(TAG_9FC81A);
    }

    public void testParseListOfTags() throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(20);
        byteBuffer.put(TAG_E1);
        byteBuffer.put(TAG_9F39);
        byteBuffer.put(TAG_9F1A);
        byteBuffer.put(TAG_9FC81A);
        int endPosition = byteBuffer.position();
        byteBuffer.flip();
        BerTlvUtils.assertEquals(TAG_E1, Tag.parse(byteBuffer).getBytes());
        BerTlvUtils.assertEquals(TAG_9F39, Tag.parse(byteBuffer).getBytes());
        BerTlvUtils.assertEquals(TAG_9F1A, Tag.parse(byteBuffer).getBytes());
        BerTlvUtils.assertEquals(TAG_9FC81A, Tag.parse(byteBuffer).getBytes());
        assertEquals(endPosition, byteBuffer.position());
    }

    private void doTestParse(byte[] rawTag) {
    	ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        byteBuffer.put(rawTag);
        Tag parsedTag = Tag.parse((ByteBuffer) byteBuffer.flip());
        BerTlvUtils.assertEquals(rawTag, parsedTag.getBytes());
        assertEquals(rawTag.length, byteBuffer.position());
    }
}
