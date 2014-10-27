package io.github.binaryfoo

import io.github.binaryfoo.decoders.*
import io.github.binaryfoo.tlv.Tag

import java.util.HashMap
import kotlin.platform.platformStatic
import java.io.IOException
import java.io.PrintWriter
import java.io.FileWriter
import io.github.binaryfoo.io.ClasspathIO
import org.yaml.snakeyaml.Yaml
import java.util.LinkedHashMap
import org.yaml.snakeyaml.Loader
import org.yaml.snakeyaml.resolver.Resolver
import org.yaml.snakeyaml.Dumper

/**
 * A set of rules for interpreting a set of tags.
 */

public class TagMetaData(private val metadata: MutableMap<String, TagInfo>) {

    public fun put(tag: Tag, tagInfo: TagInfo) {
        put(tag.getHexString(), tagInfo)
    }

    private fun put(tag: String, tagInfo: TagInfo) {
        if (metadata.put(tag, tagInfo) != null) {
            throw IllegalArgumentException("Duplicate entry for $tag")
        }
    }

    public fun newTag(hexString: String, shortName: String, longName: String, primitiveDecoder: PrimitiveDecoder): Tag {
        val tag = Tag.fromHex(hexString)
        put(tag, TagInfo(shortName, longName, Decoders.PRIMITIVE, primitiveDecoder))
        return tag
    }

    public fun newTag(hexString: String, shortName: String, longName: String, decoder: Decoder): Tag {
        val tag = Tag.fromHex(hexString)
        put(tag, TagInfo(shortName, longName, decoder, PrimitiveDecoder.HEX))
        return tag
    }

    public fun get(tag: Tag): TagInfo {
        val tagInfo = metadata.get(tag.getHexString())
        if (tagInfo == null) {
            return TagInfo("?", "?", Decoders.PRIMITIVE, PrimitiveDecoder.HEX)
        }
        return tagInfo
    }

    public fun join(other: TagMetaData): TagMetaData {
        val joined = copy(other)
        for ((tag, info) in metadata) {
            joined.put(tag, info)
        }
        return joined
    }

    class object {
        platformStatic public fun empty(): TagMetaData {
            return TagMetaData(HashMap())
        }

        platformStatic public fun copy(metadata: TagMetaData): TagMetaData {
            return TagMetaData(HashMap(metadata.metadata))
        }

        platformStatic public fun load(name: String): TagMetaData {
            return TagMetaData(LinkedHashMap((Yaml(Loader(), Dumper(), Resolver(false)).load(ClasspathIO.open(name)) as Map<String, Map<String, String?>>).mapValues {
                val shortName = it.value["name"]!!
                val longName = it.value.getOrElse("longName", {shortName})!!
                val decoder: Decoder = if (it.value.contains("decoder")) {
                    Class.forName("io.github.binaryfoo.decoders." + it.value["decoder"]).newInstance() as Decoder
                } else {
                    Decoders.PRIMITIVE
                }
                val primitiveDecoder = if (it.value.contains("primitiveDecoder")) {
                    Class.forName("io.github.binaryfoo.decoders." + it.value["primitiveDecoder"]).newInstance() as PrimitiveDecoder
                } else {
                    PrimitiveDecoder.HEX
                }
                TagInfo(shortName, longName, decoder, primitiveDecoder, it.value.get("short"), it.value.get("long"))
            }))
        }

        public fun toYaml(fileName: String, meta: TagMetaData, scheme: String) {
            PrintWriter(FileWriter(fileName)).use { writer ->
                for (e in meta.metadata.entrySet()) {
                    writer.println(e.getKey() + ":")
                    val tagInfo = e.getValue()
                    writer.println(" name: " + tagInfo.shortName)
                    if (tagInfo.shortName != tagInfo.longName) {
                        writer.println(" longName: " + tagInfo.longName)
                    }
                    if (tagInfo.decoder != Decoders.PRIMITIVE) {
                        writer.println(" decoder: " + tagInfo.decoder.javaClass.getSimpleName())
                    }
                    if (tagInfo.primitiveDecoder != PrimitiveDecoder.HEX) {
                        writer.println(" primitiveDecoder: " + tagInfo.primitiveDecoder.javaClass.getSimpleName())
                    }
                    writer.println(" scheme: $scheme")
                    writer.println()
                }
            }
        }
    }
}

