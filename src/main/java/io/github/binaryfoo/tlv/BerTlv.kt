package io.github.binaryfoo.tlv

import java.nio.ByteBuffer
import java.util.ArrayList
import java.util.Arrays
import kotlin.collections.firstOrNull

/**
 * Model data elements encoded using the Basic Encoding Rules: http://en.wikipedia.org/wiki/X.690#BER_encoding.
 */
public abstract class BerTlv(public val tag: Tag) {

    public fun toBinary(): ByteArray {
        val value = getValue()
        val encodedTag = tag.bytes
        val encodedLength = getLength(value)

        val b = ByteBuffer.allocate(encodedTag.size + encodedLength.size + value.size)
        b.put(encodedTag.toByteArray())
        b.put(encodedLength)
        b.put(value)
        b.flip()
        return b.array()
    }

    /**
     * The whole object (the T, L and V components) as a hex string.
     */
    public fun toHexString(): String = ISOUtil.hexString(toBinary())

    /**
     * The value of V in TLV as a hex string.
     */
    public val valueAsHexString: String
    get() = ISOUtil.hexString(getValue())

    /**
     * The number of bytes used to encode the L (length) in TLV.
     * Eg 1 byte might be used to encode a length of 12, whilst at least 2 bytes would be used for a length of 300.
     */
    public val lengthInBytesOfEncodedLength: Int
    get() = getLength(getValue()).size

    /**
     * The value of L (length) in TLV. Length in bytes of the value.
     */
    public val length: Int
    get() = getValue().size

    /**
     * Skip the tag and length bytes.
     */
    public val startIndexOfValue: Int
    get() = tag.bytes.size + lengthInBytesOfEncodedLength

    public abstract fun findTlv(tag: Tag): BerTlv?

    public abstract fun findTlvs(tag: Tag): List<BerTlv>

    /**
     * The value of V in TLV as a byte array.
     */
    public abstract fun getValue(): ByteArray

    /**
     * For a constructed TLV the child elements that make up the V. For a primitive, an empty list.
     */
    public abstract fun getChildren(): List<BerTlv>

    private fun getLength(value: ByteArray?): ByteArray {
        val length: ByteArray
        if (value == null) {
            return byteArrayOf(0.toByte())
        }
        if (value.size <= 0x7F) {
            length = byteArrayOf(value.size.toByte())
        } else {
            val wanted = value.size
            var expected = 256
            var needed = 1
            while (wanted >= expected) {
                needed++
                expected = expected shl 8
                if (expected == 0) {
                    // just to be sure
                    throw IllegalArgumentException()
                }
            }
            length = ByteArray(needed + 1)
            length[0] = (0x80 or needed).toByte()
            for (i in 1..length.size - 1) {
                length[length.size - i] = ((wanted shr (8 * (i - 1))) and 255).toByte()
            }

        }
        return length
    }

    companion object {

        @JvmStatic public fun newInstance(tag: Tag, value: ByteArray): BerTlv {
            return PrimitiveBerTlv(tag, value)
        }

        @JvmStatic public fun newInstance(tag: Tag, hexString: String): BerTlv {
            return PrimitiveBerTlv(tag, ISOUtil.hex2byte(hexString))
        }

        @JvmStatic public fun newInstance(tag: Tag, value: Int): BerTlv {
            if (value > 255) {
                throw IllegalArgumentException("Value greater than 255 must be encoded in a byte array")
            }
            return PrimitiveBerTlv(tag, byteArrayOf(value.toByte()))
        }

        @JvmStatic public fun newInstance(tag: Tag, value: List<BerTlv>): BerTlv {
            return ConstructedBerTlv(tag, value)
        }

        @JvmStatic public fun newInstance(tag: Tag, tlv1: BerTlv, tlv2: BerTlv): BerTlv {
            return ConstructedBerTlv(tag, Arrays.asList<BerTlv>(tlv1, tlv2))
        }

        @JvmStatic public fun parse(data: ByteArray): BerTlv {
            return parseList(ByteBuffer.wrap(data), true)[0]
        }

        @JvmStatic public fun parseAsPrimitiveTag(data: ByteArray): BerTlv {
            return parseList(ByteBuffer.wrap(data), false)[0]
        }

        @JvmStatic public fun parseList(data: ByteArray, parseConstructedTags: Boolean): List<BerTlv> {
            return parseList(ByteBuffer.wrap(data), parseConstructedTags)
        }

        @JvmStatic public fun parseList(data: ByteArray, parseConstructedTags: Boolean, recognitionMode: TagRecognitionMode): List<BerTlv> {
            return parseList(ByteBuffer.wrap(data), parseConstructedTags, recognitionMode)
        }

        private fun parseList(data: ByteBuffer, parseConstructedTags: Boolean, recognitionMode: TagRecognitionMode = CompliantTagMode): List<BerTlv> {
            val tlvs = ArrayList<BerTlv>()

            while (data.hasRemaining()) {
                val tag = Tag.parse(data, recognitionMode)
                if (isPaddingByte(tag)) {
                    continue
                }
                try {
                    val length = parseLength(data)
                    val value = readUpToLength(data, length)
                    if (tag.constructed && parseConstructedTags) {
                        try {
                            tlvs.add(newInstance(tag, parseList(value, true, recognitionMode)))
                        } catch (e: Exception) {
                            tlvs.add(newInstance(tag, value))
                        }

                    } else {
                        tlvs.add(newInstance(tag, value))
                    }
                } catch (e: Exception) {
                    throw TlvParseException(tlvs, "Failed parsing TLV with tag $tag: " + e.message, e)
                }

            }
            return tlvs
        }

        private fun readUpToLength(data: ByteBuffer, length: Int): ByteArray {
            val value = ByteArray(if (length > data.remaining()) data.remaining() else length)
            data.get(value)
            return value
        }

        // Specification Update No. 69, 2009, Padding of BER-TLV Encoded Constructed Data Objects
        private fun isPaddingByte(tag: Tag): Boolean {
            return tag.bytes.size == 1 && tag.bytes[0] == 0.toByte()
        }

        private fun parseLength(data: ByteBuffer): Int {
            val firstByte = data.get().toInt()
            var length = 0
            if ((firstByte and 0x80) == 0x80) {
                var numberOfBytesToEncodeLength = (firstByte and 0x7F)
                for (i in 1..numberOfBytesToEncodeLength) {
                    if (!data.hasRemaining()) {
                        throw IllegalArgumentException("Bad length: expected to read $numberOfBytesToEncodeLength (0x${firstByte.toByte().toHexString()}) bytes. Only have ${i-1}.")
                    }
                    length += (data.get().toInt() and 0xFF)
                    if (i != numberOfBytesToEncodeLength) {
                        length *= 256
                    }
                    if (length < 0) {
                        throw IllegalArgumentException("Bad length: $length < 0. Read $i of $numberOfBytesToEncodeLength (0x${firstByte.toByte().toHexString()}) bytes used to encode length of TLV.")
                    }
                }
            } else {
                length = firstByte
            }
            return length
        }

        @JvmStatic public fun findTlv(tlvs: List<BerTlv>, tag: Tag): BerTlv? = tlvs.firstOrNull { it.tag == tag }
    }

}
