package io.github.binaryfoo

import io.github.binaryfoo.decoders.annotator.BackgroundReading
import io.github.binaryfoo.hex.HexDumpElement
import io.github.binaryfoo.tlv.BerTlv
import io.github.binaryfoo.tlv.ISOUtil
import io.github.binaryfoo.tlv.Tag
import org.apache.commons.lang.StringUtils
import java.util.*

/**
 * A rather oddly named class that attempts to order a description of the bits (a decoding) into a hierarchy.
 * Each instance represents a single level in the hierarchy. Leaf nodes have no children.
 *
 * Examples:
 * <ul>
 *     <li>A TLV object with children</li>
 *     <li>A primitive value interpreted as a bit string, like TVR, where the children are the "on" bits.</li>
 * </ul>
 */
data class DecodedData(
    val tag: Tag?,
    val rawData: String, // Not a great name. Used as a label for the decoded data.
    val fullDecodedData: String,
    val startIndex: Int = 0, // in bytes
    val endIndex: Int = 0, // in bytes
    var kids: List<DecodedData> = listOf(),
    var backgroundReading: Map<String, String?>? = null, // wordy explanation. Eg to show in tooltip/popover,
    val tlv: BerTlv? = null,
    var category: String = ""
) {
  var hexDump: List<HexDumpElement>? = null
  private val _children: MutableList<DecodedData> = ArrayList(kids)
  val children: List<DecodedData>
    get() {
      return _children
    }

  /**
   * Position within the #hexDump of the bytes this decoding represents.
   * For a decoding of a TLV this will include the T, L and V components. The whole kit and caboodle.
   */
  val positionInHexDump: ClosedRange<Int>
    get() {
      return startIndex..endIndex - 1
    }

  /**
   * Position within the #hexDump of the bytes comprising the T (tag) in the TLV this decoding represents.
   * Null if this decoding didn't come from a TLV.
   */
  val tagPositionInHexDump: ClosedRange<Int>?
    get() {
      return if (tlv != null) startIndex..(startIndex + tlv.tag.bytes.size - 1) else null
    }

  /**
   * Position within the #hexDump of the bytes comprising the L (length) in the TLV this decoding represents.
   * Null if this decoding didn't come from a TLV.
   */
  val lengthPositionInHexDump: ClosedRange<Int>?
    get() {
      return if (tlv != null) {
        val firstLengthByte = tagPositionInHexDump!!.endInclusive + 1
        firstLengthByte..(firstLengthByte + tlv.lengthInBytesOfEncodedLength - 1)
      } else {
        null
      }
    }

  private fun trim(decodedData: String): String {
    return if (decodedData.length >= 60) decodedData.substring(0, 56) + "..." + StringUtils.right(decodedData, 4) else decodedData
  }

  /**
   * For an element with children this is usually the hex value (ie the real raw data).
   * For an element without children this is the decoded value: ascii, numeric, enumerated or even just hex.
   */
  fun getDecodedData(): String {
    return if (isComposite()) trim(fullDecodedData) else fullDecodedData
  }

  fun getChild(index: Int): DecodedData {
    return children[index]
  }

  fun isComposite(): Boolean {
    return !children.isEmpty()
  }

  /**
   * For values that can't be decoded in the first pass. Like decrypted ones.
   */
  fun addChildren(child: List<DecodedData>) {
    _children.addAll(child)
  }

  override fun toString(): String {
    var s = "raw=[$rawData] decoded=[$fullDecodedData] indexes=[$startIndex,$endIndex]"
    if (backgroundReading != null) {
      s += " background=[$backgroundReading]"
    }
    if (tag != null) {
      s += " tag=$tag"
    }
    if (tlv != null) {
      s += " tlv=$tlv"
    }
    if (isComposite()) {
      for (d in children) {
        s += "\n" + d
      }
    }
    return s
  }

  companion object {

    @JvmStatic
    fun primitive(rawData: String, decodedData: String, startIndex: Int = 0, endIndex: Int = 0): DecodedData {
      return DecodedData(null, rawData, decodedData, startIndex, endIndex, backgroundReading = BackgroundReading.readingFor(rawData))
    }

    @JvmStatic
    fun byteRange(rawData: String, bytes: ByteArray, startIndexWithinBytes: Int, length: Int, startIndexWithinFullDecoding: Int): DecodedData {
      val decodedData = ISOUtil.hexString(bytes, startIndexWithinBytes, length)
      return DecodedData(null, rawData, decodedData, startIndexWithinFullDecoding + startIndexWithinBytes, startIndexWithinFullDecoding + startIndexWithinBytes + length, listOf<DecodedData>())
    }

    @JvmStatic
    fun byteRange(rawData: String, decodedData: String, startIndexWithinBytes: Int, length: Int, startIndexWithinFullDecoding: Int): DecodedData {
      return DecodedData(null, rawData, decodedData, startIndexWithinFullDecoding + startIndexWithinBytes, startIndexWithinFullDecoding + startIndexWithinBytes + length, listOf<DecodedData>())
    }

    @JvmStatic
    fun constructed(rawData: String, decodedData: String, startIndex: Int = 0, endIndex: Int = 0, children: List<DecodedData>): DecodedData {
      return DecodedData(null, rawData, decodedData, startIndex, endIndex, children, BackgroundReading.readingFor(rawData))
    }

    /**
     * Attach a Tag but the data wasn't actually encoded as TLV. Eg a bunch of values are concatenated in a stream.
     */
    @JvmStatic
    fun withTag(tag: Tag, metadata: TagMetaData, decodedData: String, startIndex: Int, endIndex: Int, children: List<DecodedData> = listOf()): DecodedData {
      val tagInfo = metadata.get(tag)
      return DecodedData(tag, tag.toString(tagInfo), decodedData, startIndex, endIndex, children, tagInfo.backgroundReading)
    }

    /**
     * Decoded from a TLV.
     */
    @JvmStatic
    fun fromTlv(tlv: BerTlv, metadata: TagMetaData, decodedData: String, startIndex: Int, endIndex: Int, children: List<DecodedData> = listOf()): DecodedData {
      val tag = tlv.tag
      val tagInfo = metadata.get(tag)
      return DecodedData(tag, tag.toString(tagInfo), decodedData, startIndex, endIndex, children, tagInfo.backgroundReading, tlv)
    }

    @JvmStatic
    fun findForTag(tag: Tag, decoded: List<DecodedData>): DecodedData? {
      val matches = decoded.findAllForTag(tag)
      return if (matches.isEmpty()) null else matches[0]
    }

    @JvmStatic
    fun findAllForTag(tag: Tag, decoded: List<DecodedData>): List<DecodedData> {
      var matches = ArrayList<DecodedData>()
      decoded.forEach {
        if (it.tag == tag) {
          matches.add(it)
        }
        matches.addAll(it.children.findAllForTag(tag))
      }
      return matches
    }

    @JvmStatic
    fun findForValue(value: String, decoded: List<DecodedData>): DecodedData? {
      val matches = decoded.findAllForValue(value)
      return if (matches.isEmpty()) null else matches[0]
    }

    @JvmStatic
    fun findAllForValue(value: String, decoded: List<DecodedData>): List<DecodedData> {
      var matches = ArrayList<DecodedData>()
      decoded.forEach {
        if (it.fullDecodedData == value) {
          matches.add(it)
        }
        matches.addAll(it.children.findAllForValue(value))
      }
      return matches
    }

  }

}

fun List<DecodedData>.findForTag(tag: Tag): DecodedData? {
  return DecodedData.findForTag(tag, this)
}

fun List<DecodedData>.findTlvForTag(tag: Tag): BerTlv? {
  return DecodedData.findAllForTag(tag, this).lastOrNull()?.tlv
}

fun List<DecodedData>.findValueForTag(tag: Tag): String? {
  return DecodedData.findAllForTag(tag, this).lastOrNull()?.tlv?.valueAsHexString
}

fun List<DecodedData>.findAllForTag(tag: Tag): List<DecodedData> {
  return DecodedData.findAllForTag(tag, this)
}

fun List<DecodedData>.findForValue(value: String): DecodedData? {
  return DecodedData.findForValue(value, this)
}

fun List<DecodedData>.findAllForValue(value: String): List<DecodedData> {
  return DecodedData.findAllForValue(value, this)
}

fun List<DecodedData>.toSimpleString(indent: String = ""): String {
  val b = StringBuilder()
  for (d in this) {
    b.append(indent)
    val decodedData = d.getDecodedData()
    if ("" != d.rawData) {
      b.append(d.rawData).append(":")
      if ("" != decodedData) {
        b.append(" ")
      }
    }
    b.append(decodedData).append("\n")
    b.append(d.children.toSimpleString(indent + "  "))
  }
  return b.toString()
}
