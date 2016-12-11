package io.github.binaryfoo.decoders.annotator

import io.github.binaryfoo.decoders.apdu.APDUCommand
import io.github.binaryfoo.res.ClasspathIO
import org.yaml.snakeyaml.Yaml
import kotlin.collections.mapOf

/**
 * Some english description for each field. From a set of .yaml files.
 */
class BackgroundReading {

    companion object {

        @JvmStatic fun readingFor(apdu: APDUCommand): Map<String, String?>? {
            return apduDescriptions["$apdu"]
        }


        @JvmStatic fun readingFor(field: String): Map<String, String?>? {
            return descriptions[field]
        }

        private val apduDescriptions: Map<String, Map<String, String?>> by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            @Suppress("UNCHECKED_CAST")
            (Yaml().load(ClasspathIO.open("apdus.yaml")) as Map<String, Map<String, String?>>)
        }

        private val descriptions: Map<String, Map<String, String?>> by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            @Suppress("UNCHECKED_CAST")
            (Yaml().load(ClasspathIO.open("descriptions.yaml")) as Map<String, Map<String, String?>>)
        }

    }
}
fun backgroundOf(short: String, long: String? = null): Map<String, String?> = mapOf("short" to short, "long" to long)
