package io.github.binaryfoo.tlv

import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.Arrays

import io.github.binaryfoo.TagMetaData
import org.apache.commons.lang.builder.HashCodeBuilder
import kotlin.platform.platformStatic

public data class Tag(val bytes: ByteArray) {

    {
        validate(bytes)
    }

    private fun validate(b: ByteArray?) {
        if (b == null || b.size == 0) {
            throw IllegalArgumentException("Tag must be constructed with a non-empty byte array")
        }
        if (b.size == 1) {
            if ((b[0].toInt() and 0x1F) == 0x1F) {
                throw IllegalArgumentException("If bit 6 to bit 1 are set tag must not be only one byte long")
            }
        } else {
            if ((b[b.size - 1].toInt() and 0x80) != 0) {
                throw IllegalArgumentException("For multibyte tag bit 8 of the final byte must be 0: " + Integer.toHexString(b[b.size - 1].toInt()))
            }
            if (b.size > 2) {
                for (i in 1..b.size - 2) {
                    if ((b[i].toInt() and 0x80) != 0x80) {
                        throw IllegalArgumentException("For multibyte tag bit 8 of the internal bytes must be 1: " + Integer.toHexString(b[i].toInt()))
                    }
                }
            }
        }
    }

    public fun getHexString(): String {
        return ISOUtil.hexString(bytes)
    }

    public fun isConstructed(): Boolean {
        return (bytes[0].toInt() and 0x20) == 0x20
    }

    override fun toString(): String {
        return ISOUtil.hexString(bytes)
    }

    public fun toString(tagMetaData: TagMetaData): String {
        return ISOUtil.hexString(bytes) + " (" + tagMetaData.get(this).fullName + ")"
    }

    class object {

        platformStatic public fun fromHex(hexString: String): Tag {
            return Tag(ISOUtil.hex2byte(hexString))
        }

        platformStatic public fun parse(buffer: ByteBuffer): Tag {
            val out = ByteArrayOutputStream()
            var b = buffer.get()
            out.write(b.toInt())
            if ((b.toInt() and 0x1F) == 0x1F) {
                do {
                    b = buffer.get()
                    out.write(b.toInt())
                } while ((b.toInt() and 0x80) == 0x80)
            }
            return Tag(out.toByteArray())
        }
    }
}
