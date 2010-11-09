package com.willcurrie.tlv;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.apache.commons.lang.builder.HashCodeBuilder;

import com.willcurrie.EmvTags;

public class Tag {

    private final byte[] bytes;

    public static Tag fromHex(String hexString) {
        return new Tag(ISOUtil.hex2byte(hexString));
    }

    public Tag(byte[] bytes) {
        validate(bytes);
        this.bytes = bytes;
    }

    private void validate(byte[] b) {
        if (b == null || b.length == 0) {
            throw new IllegalArgumentException("Tag must be constructed with a non-empty byte array");
        }
        if (b.length == 1) {
            if ((b[0] & (byte) 0x1F) == (byte) 0x1F) {
                throw new IllegalArgumentException("If bit 6 to bit 1 are set tag must not be only one byte long");
            }
        } else {
            if ((b[b.length - 1] & (byte) 0x80) != (byte) 0x00) {
                throw new IllegalArgumentException("For multibyte tag bit 8 of the final byte must be 0: " + Integer.toHexString(b[b.length - 1]));
            }
            if (b.length > 2) {
                for (int i = 1; i < b.length - 1; i++) {
                    if ((b[i] & (byte) 0x80) != (byte) 0x80) {
                        throw new IllegalArgumentException("For multibyte tag bit 8 of the internal bytes must be 1: " + Integer.toHexString(b[i]));
                    }
                }
            }
        }
    }

    public byte[] getBytes() {
        return bytes;
    }

    public String getHexString() {
    	return ISOUtil.hexString(bytes);
    }
    
    public boolean isConstructed() {
        return (bytes[0] & 0x20) == 0x20;
    }

    public static Tag parse(ByteBuffer buffer) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte b = buffer.get();
        out.write(b);
        if ((b & 0x1F) == 0x1F) {
            do {
                b = buffer.get();
                out.write(b);
            } while ((b & 0x80) == 0x80);
        }
        return new Tag(out.toByteArray());
    }

    public boolean equals(Object obj) {
        if (obj instanceof Tag) {
            Tag other = (Tag) obj;
            return Arrays.equals(bytes, other.bytes);
        }
        return false;
    }

    public int hashCode() {
        return new HashCodeBuilder().append(bytes).toHashCode();
    }

    public String toString() {
        return ISOUtil.hexString(bytes) + " (" + EmvTags.getTagName(this) + ")";
    }
}
