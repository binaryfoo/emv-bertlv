package io.github.binaryfoo.decoders.annotator

import org.yaml.snakeyaml.Yaml
import io.github.binaryfoo.res.ClasspathIO
import kotlin.properties.Delegates
import java.util.HashMap
import io.github.binaryfoo.decoders.apdu.APDUCommand
import kotlin.platform.platformStatic
import io.github.binaryfoo.tlv.Tag

public class BackgroundReading {

    class object {

        platformStatic public fun readingFor(apdu: APDUCommand): Map<String, String?>? {
            return apduDescriptions["$apdu"]
        }


        platformStatic public fun readingFor(field: String): Map<String, String?>? {
            return descriptions["$field"]
        }

        private val apduDescriptions: Map<String, Map<String, String?>> by Delegates.blockingLazy {
            Yaml().load(ClasspathIO.open("apdus.yaml")) as Map<String, Map<String, String?>>
        }

        private val descriptions: Map<String, Map<String, String?>> by Delegates.blockingLazy {
            Yaml().load(ClasspathIO.open("descriptions.yaml")) as Map<String, Map<String, String?>>
        }

    }
}
public fun backgroundOf(short: String, long: String? = null): Map<String, String?> = mapOf("short" to short, "long" to long)
