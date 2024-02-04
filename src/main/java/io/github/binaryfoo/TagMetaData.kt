package io.github.binaryfoo

import io.github.binaryfoo.decoders.Decoders
import io.github.binaryfoo.decoders.PrimitiveDecoder
import io.github.binaryfoo.res.ClasspathIO
import io.github.binaryfoo.tlv.Tag
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.representer.Representer
import org.yaml.snakeyaml.resolver.Resolver
import java.io.FileWriter
import java.io.PrintWriter
import kotlin.collections.*

/**
 * A set of rules for interpreting a set of tags.
 */

class TagMetaData(private val metadata: MutableMap<String, TagInfo>) {

  fun put(tag: Tag, tagInfo: TagInfo) {
    put(tag.hexString, tagInfo)
  }

  private fun put(tag: String, tagInfo: TagInfo) {
    if (metadata.put(tag, tagInfo) != null) {
      throw IllegalArgumentException("Duplicate entry for $tag")
    }
  }

  fun newTag(hexString: String, shortName: String, longName: String, primitiveDecoder: PrimitiveDecoder): Tag {
    val tag = Tag.fromHex(hexString)
    put(tag, TagInfo(shortName, longName, Decoders.PRIMITIVE, primitiveDecoder))
    return tag
  }

  fun newTag(hexString: String, shortName: String, longName: String, decoder: Decoder): Tag {
    val tag = Tag.fromHex(hexString)
    put(tag, TagInfo(shortName, longName, decoder, PrimitiveDecoder.HEX))
    return tag
  }

  fun get(tag: Tag): TagInfo {
    return metadata[tag.hexString]
        ?: return TagInfo("?", "?", Decoders.PRIMITIVE, PrimitiveDecoder.HEX)
  }

  fun join(other: TagMetaData): TagMetaData {
    val joined = copy(other)
    for ((tag, info) in metadata) {
      joined.put(tag, info)
    }
    return joined
  }

  companion object {
    @JvmStatic
    fun empty(): TagMetaData {
      return TagMetaData(HashMap())
    }

    @JvmStatic
    fun copy(metadata: TagMetaData): TagMetaData {
      return TagMetaData(HashMap(metadata.metadata))
    }

    @JvmStatic
    fun load(name: String): TagMetaData {
      val yaml = Yaml(Constructor(LoaderOptions()), Representer(DumperOptions()), DumperOptions(), object : Resolver() {
        override fun addImplicitResolvers() {
          // leave everything as strings
        }
      })
      @Suppress("UNCHECKED_CAST")
      val map = yaml.load(ClasspathIO.open(name)) as Map<String, Map<String, String?>>
      return TagMetaData(LinkedHashMap(map.mapValues {
        val shortName = it.value["name"]!!
        val longName = it.value["longName"] ?: shortName
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
        TagInfo(shortName, longName, decoder, primitiveDecoder, it.value["short"], it.value["long"])
      }))
    }

    fun toYaml(fileName: String, meta: TagMetaData, scheme: String) {
      PrintWriter(FileWriter(fileName)).use { writer ->
        for (e in meta.metadata.entries) {
          writer.println(e.key + ":")
          val tagInfo = e.value
          writer.println(" name: " + tagInfo.shortName)
          if (tagInfo.shortName != tagInfo.longName) {
            writer.println(" longName: " + tagInfo.longName)
          }
          if (tagInfo.decoder != Decoders.PRIMITIVE) {
            writer.println(" decoder: " + tagInfo.decoder.javaClass.simpleName)
          }
          if (tagInfo.primitiveDecoder != PrimitiveDecoder.HEX) {
            writer.println(" primitiveDecoder: " + tagInfo.primitiveDecoder.javaClass.simpleName)
          }
          writer.println(" scheme: $scheme")
          writer.println()
        }
      }
    }
  }
}

