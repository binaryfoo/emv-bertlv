package io.github.binaryfoo.decoders.annotator

import org.yaml.snakeyaml.Yaml
import io.github.binaryfoo.io.ClasspathIO
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

        private val apduDescriptions: Map<String, Map<String, String?>> by Delegates.blockingLazy {
            Yaml().load(ClasspathIO.open("apdus.yaml")) as Map<String, Map<String, String?>>
        }

        platformStatic public fun readingFor(tag: Tag): Map<String, String?>? {
            return tagDescriptions["$tag"]
        }

        private val tagDescriptions: Map<String, Map<String, String?>> by Delegates.blockingLazy {
            Yaml().load(ClasspathIO.open("tags.yaml")) as Map<String, Map<String, String?>>
        }
    }
}
