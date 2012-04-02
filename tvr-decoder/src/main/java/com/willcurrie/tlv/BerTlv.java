package com.willcurrie.tlv;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BerTlv {

    private final Tag tag;

    public BerTlv(Tag tag) {
        this.tag = tag;
    }

    public static BerTlv newInstance(Tag tag, byte[] value) {
        return new PrimitiveBerTlv(tag, value);
    }

    public static BerTlv newInstance(Tag tag, String hexString) {
        return new PrimitiveBerTlv(tag, ISOUtil.hex2byte(hexString));
    }

    public static BerTlv newInstance(Tag tag, int value) {
        if (value > 255) {
            throw new IllegalArgumentException("Value greater than 255 must be encode in a byte array");
        }
        return new PrimitiveBerTlv(tag, new byte[] {(byte) value});
    }

    public static BerTlv newInstance(Tag tag, List<BerTlv> value) {
        return new ConstructedBerTlv(tag, value);
    }

    public static BerTlv newInstance(Tag tag, BerTlv tlv1, BerTlv tlv2) {
        return new ConstructedBerTlv(tag, Arrays.asList(tlv1, tlv2));
    }

    public static BerTlv parse(byte[] data) {
        List<BerTlv> tlvs = parseList(ByteBuffer.wrap(data), true);
        return tlvs.get(0);
    }

    public static BerTlv parseAsPrimitiveTag(byte[] data) {
        List<BerTlv> tlvs = parseList(ByteBuffer.wrap(data), false);
        return tlvs.get(0);
    }

    public static List<BerTlv> parseList(byte[] data, boolean parseConstructedTags) {
        return parseList(ByteBuffer.wrap(data), parseConstructedTags);
    }

    private static List<BerTlv> parseList(ByteBuffer data, boolean parseConstructedTags) {
        List<BerTlv> tlvs = new ArrayList<BerTlv>();

        while (data.hasRemaining()) {
            Tag tag = Tag.parse(data);
            try {
                int length = parseLength(data);

                byte[] value = new byte[length];
                data.get(value);
                if (tag.isConstructed() && parseConstructedTags) {
                    try {
                        tlvs.add(newInstance(tag, parseList(value, parseConstructedTags)));
                    } catch (Exception e) {
                        tlvs.add(newInstance(tag, value));
                    }
                } else {
                    tlvs.add(newInstance(tag, value));
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed parsing " + tag + "," + (e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage()), e);
            }
        }
        return tlvs;
    }

    private static int parseLength(ByteBuffer data) {
        int lengthByte = data.get();
        int dataLength = 0;
        if ((lengthByte & 0x80) == 0x80) {
            int numberOfBytesToEncodeLength = (lengthByte & 0x7F);
            while (numberOfBytesToEncodeLength > 0) {
                dataLength += data.get() & 0xFF;

                if (numberOfBytesToEncodeLength > 1) {
                    dataLength *= 256;
                }
                numberOfBytesToEncodeLength--;
            }
        } else {
            dataLength = lengthByte;
        }
        return dataLength;
    }

    public static BerTlv findTlv(List<BerTlv> tlvs, Tag tag) {
        for (BerTlv tlv : tlvs) {
            if (tlv.getTag().equals(tag)) {
                return tlv;
            }
        }
        return null;
    }

    public Tag getTag() {
        return tag;
    }

    public byte[] toBinary() {
        byte[] value = getValue();
        byte[] encodedTag = tag.getBytes();
        byte[] encodedLength = getLength(value);

        ByteBuffer b = ByteBuffer.allocate(encodedTag.length + encodedLength.length + value.length);
        b.put(encodedTag);
        b.put(encodedLength);
        b.put(value);
        b.flip();
        return b.array();
    }

    public String toHexString() {
        return ISOUtil.hexString(toBinary());
    }

    public String getValueAsHexString() {
        return ISOUtil.hexString(getValue());
    }

    public int getLengthInBytesOfEncodedLength() {
    	return getLength(getValue()).length;
    }
    
    public abstract BerTlv findTlv(Tag tag);

    public abstract List<BerTlv> findTlvs(Tag tag);

    public abstract byte[] getValue();

    public abstract List<BerTlv> getChildren();

    private byte[] getLength(byte[] value) {
        byte[] length = null;
        if (value == null) {
            return new byte[] {(byte) 0x00};
        }
        if (value.length <= 127) {
            length = new byte[] {(byte) value.length};
        } else {
            int wanted = value.length;
            int expected = 0x100;
            int needed = 1;
            while (!(wanted < expected)) {
                needed++;
                expected = expected << 8;
                if (expected == 0) {
                    // just to be sure
                    throw new IllegalArgumentException();
                }
            }
            length = new byte[needed + 1];
            length[0] = (byte) (0x80 | needed);
            for (int i = 1; i < length.length; i++) {
                length[length.length - i] = (byte) ((wanted >> (8 * (i - 1))) & 0xFF);
            }

        }
        return length;
    }

}

