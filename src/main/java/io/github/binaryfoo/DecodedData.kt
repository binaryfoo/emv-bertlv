package io.github.binaryfoo

import java.util.Collections

import io.github.binaryfoo.hex.HexDumpElement
import io.github.binaryfoo.tlv.Tag
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder
import kotlin.platform.platformStatic
import java.util.ArrayList
import java.util.LinkedList

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
public data class DecodedData(
        val tag: Tag?,
        val rawData: String, // Not a great name. Used as a label for the decoded data.
        val fullDecodedData: String,
        val startIndex: Int, // in bytes
        val endIndex: Int, // in bytes
        kids: List<DecodedData> = listOf(),
        var backgroundReading: Map<String, String?>? = null // wordy explanation. Eg to show in tooltip/popover
) {
    /**
     * Allow Command and Response APDUs to be displayed specially.
     */
    public var category: String = ""
    public var hexDump: List<HexDumpElement>? = null
    private val _children: MutableList<DecodedData> = ArrayList(kids)
    public val children: List<DecodedData>
    get() {
        return _children
    }

    private fun trim(decodedData: String): String {
        return if (decodedData.length() >= 60) decodedData.substring(0, 56) + "..." + StringUtils.right(decodedData, 4) else decodedData
    }

    /**
     * For an element with children this is usually the hex value (ie the real raw data).
     * For an element without children this is the decoded value: ascii, numeric, enumerated or even just hex.
     */
    public fun getDecodedData(): String {
        return if (isComposite()) trim(fullDecodedData) else fullDecodedData
    }

    public fun getChild(index: Int): DecodedData {
        return children.get(index)
    }

    public fun isComposite(): Boolean {
        return !children.isEmpty()
    }

    /**
     * For values that can't be decoded in the first pass. Like decrypted ones.
     */
    public fun addChildren(child: List<DecodedData>) {
        _children.addAll(child)
    }

    override fun toString(): String {
        var s = "raw=[${rawData}] decoded=[${fullDecodedData}] indexes=[${startIndex},${endIndex}] background=[$backgroundReading]"
        if (isComposite()) {
            for (d in children) {
                s += "\n" + d
            }
        }
        return s
    }

    class object {

        platformStatic public fun primitive(rawData: String, decodedData: String, startIndex: Int = 0, endIndex: Int = 0): DecodedData {
            return DecodedData(null, rawData, decodedData, startIndex, endIndex, listOf<DecodedData>())
        }

        platformStatic public fun constructed(rawData: String, decodedData: String, startIndex: Int = 0, endIndex: Int = 0, children: List<DecodedData>): DecodedData {
            return DecodedData(null, rawData, decodedData, startIndex, endIndex, children)
        }

        platformStatic public fun fromTlv(tag: Tag, metadata: TagMetaData, decodedData: String, startIndex: Int, endIndex: Int, children: List<DecodedData>): DecodedData {
            val tagInfo = metadata.get(tag)
            return DecodedData(tag, tag.toString(tagInfo), decodedData, startIndex, endIndex, children, tagInfo.backgroundReading)
        }

        platformStatic public fun fromTlv(tag: Tag, rawdata: String, decodedData: String, startIndex: Int, endIndex: Int, children: List<DecodedData> = listOf()): DecodedData {
            return DecodedData(tag, rawdata, decodedData, startIndex, endIndex, children)
        }

        platformStatic public fun findForTag(tag: Tag, decoded: List<DecodedData>): DecodedData? {
            val matches = decoded.findAllForTag(tag)
            return if (matches.empty) null else matches[0]
        }

        platformStatic public fun findAllForTag(tag: Tag, decoded: List<DecodedData>): List<DecodedData> {
            var matches = ArrayList<DecodedData>()
            decoded.forEach {
                if (it.tag == tag) {
                    matches.add(it)
                }
                matches.addAll(it.children.findAllForTag(tag))
            }
            return matches
        }

        platformStatic public fun findForValue(value: String, decoded: List<DecodedData>): DecodedData? {
            val matches = decoded.findAllForValue(value)
            return if (matches.empty) null else matches[0]
        }

        platformStatic public fun findAllForValue(value: String, decoded: List<DecodedData>): List<DecodedData> {
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

public fun List<DecodedData>.findForTag(tag: Tag): DecodedData? {
    return DecodedData.findForTag(tag, this)
}

public fun List<DecodedData>.findAllForTag(tag: Tag): List<DecodedData> {
    return DecodedData.findAllForTag(tag, this)
}

public fun List<DecodedData>.findForValue(value: String): DecodedData? {
    return DecodedData.findForValue(value, this)
}

public fun List<DecodedData>.findAllForValue(value: String): List<DecodedData> {
    return DecodedData.findAllForValue(value, this)
}
