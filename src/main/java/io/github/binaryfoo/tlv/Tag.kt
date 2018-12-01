package io.github.binaryfoo.tlv

import io.github.binaryfoo.TagInfo
import io.github.binaryfoo.TagMetaData
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

/**
 * The tag in T-L-V. Sometimes called Type but EMV 4.3 Book 3 - B3 Coding of the Value Field of Data Objects uses the term.
 */
data class Tag constructor(val bytes: List<Byte>, val compliant: Boolean = true) {

  constructor(bytes: ByteArray, compliant: Boolean = true) : this(bytes.toMutableList(), compliant)

  init {
    if (compliant) {
      validate(bytes)
    }
  }

  private fun validate(b: List<Byte>) {
    if (b.size == 0) {
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

  val hexString: String
    get() = ISOUtil.hexString(bytes)

  val constructed: Boolean
    get() = (bytes[0].toInt() and 0x20) == 0x20

  val byteArray: ByteArray
    get() = bytes.toByteArray()

  fun isConstructed(): Boolean = constructed

  override fun toString(): String {
    return ISOUtil.hexString(bytes)
  }

  fun toString(tagMetaData: TagMetaData): String {
    return toString(tagMetaData.get(this))
  }

  fun toString(tagInfo: TagInfo): String {
    return "${ISOUtil.hexString(bytes)} (${tagInfo.fullName})"
  }

  companion object {

    @JvmStatic
    fun fromHex(hexString: String): Tag {
      return Tag(ISOUtil.hex2byte(hexString))
    }

    @JvmStatic
    fun parse(buffer: ByteBuffer): Tag = parse(buffer, CompliantTagMode)

    @JvmStatic
    fun parse(buffer: ByteBuffer, recognitionMode: TagRecognitionMode): Tag {
      val out = ByteArrayOutputStream()
      var b = buffer.get()
      out.write(b.toInt())
      if ((b.toInt() and 0x1F) == 0x1F) {
        do {
          b = buffer.get()
          out.write(b.toInt())
        } while (recognitionMode.keepReading(b, out))
      }
      return Tag(out.toByteArray(), recognitionMode == CompliantTagMode)
    }
  }
}

interface TagRecognitionMode {
  fun keepReading(current: Byte, all: ByteArrayOutputStream): Boolean
}

/**
 * Follows EMV 4.3 Book 3, Annex B Rules for BER-TLV Data Objects to the letter.
 */
object CompliantTagMode : TagRecognitionMode {
  override fun keepReading(current: Byte, all: ByteArrayOutputStream): Boolean = (current.toInt() and 0x80) == 0x80
}

/**
 * EMV 4.3 Book 3, Annex B Rules for BER-TLV Data Objects unless it's in a list of special cases.
 */
class QuirkListTagMode(val nonStandard: Set<String>) : TagRecognitionMode {
  override fun keepReading(current: Byte, all: ByteArrayOutputStream): Boolean {
    return CompliantTagMode.keepReading(current, all) && !nonStandard.contains(all.toByteArray().toHexString())
  }
}

/**
 * Seems at least one vendor read the following and thought I'll pick 9F80 as the start of my private tag range.
 * According to Book 3 anything greater than 7F in byte two means the tag is at least 3 three bytes long, not two.
 *
 * <blockquote>
 *     The coding of primitive context-specific class data objects in the range '9F50' to '9F7F' is reserved for the payment systems.
 *     <footer>
 *     <cite>EMV 4.3 Book 3, Annex B Rules for BER-TLV Data Objects</cite>
 *     </footer>
 * </blockquote>
 */
object CommonVendorErrorMode : TagRecognitionMode {
  override fun keepReading(current: Byte, all: ByteArrayOutputStream): Boolean {
    return CompliantTagMode.keepReading(current, all) && (all.size() != 2 || !isCommonError(all.toByteArray()))
  }

  fun isCommonError(tag: ByteArray): Boolean {
    return isCommonError(tag.toMutableList())
  }

  fun isCommonError(tag: List<Byte>): Boolean {
    return tag.size > 1 && (tag[0] == 0x9F.toByte() && (tag[1].toInt() and 0xF0) == 0x80)
  }
}

fun hasCommonVendorErrorTag(tlv: BerTlv): Boolean = CommonVendorErrorMode.isCommonError(tlv.tag.bytes)
