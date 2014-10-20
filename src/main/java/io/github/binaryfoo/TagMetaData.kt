package io.github.binaryfoo

import io.github.binaryfoo.decoders.*
import io.github.binaryfoo.tlv.Tag

import java.util.HashMap
import kotlin.platform.platformStatic

/**
 * A set of rules for interpreting a set of tags.
 */

public class TagMetaData(private val metadata: MutableMap<String, TagInfo>) {

    public fun put(tag: Tag, tagInfo: TagInfo) {
        if (metadata.put(tag.getHexString(), tagInfo) != null) {
            throw IllegalArgumentException("Duplicate entry for " + tag.getHexString())
        }
    }

    public fun get(tag: Tag): TagInfo {
        val tagInfo = metadata.get(tag.getHexString())
        if (tagInfo == null) {
            return TagInfo("?", "?", Decoders.PRIMITIVE, PrimitiveDecoder.HEX)
        }
        return tagInfo
    }

    class object {
        platformStatic public fun empty(): TagMetaData {
            return TagMetaData(HashMap())
        }

        platformStatic public fun copy(metadata: TagMetaData): TagMetaData {
            return TagMetaData(HashMap(metadata.metadata))
        }

    }
}
