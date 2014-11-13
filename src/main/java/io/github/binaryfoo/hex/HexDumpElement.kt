package io.github.binaryfoo.hex

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder
import org.apache.commons.lang.builder.ToStringBuilder
import org.apache.commons.lang.builder.ToStringStyle
import java.util.ArrayList
import kotlin.platform.platformStatic

public data class HexDumpElement(val value: String, val byteOffset: Int) {

    class object {
        platformStatic public fun splitIntoByteLengthStrings(hexString: String, startIndexInBytes: Int): List<HexDumpElement> {
            val elements = ArrayList<HexDumpElement>()
            var byteOffset = startIndexInBytes
            for (i in 0..hexString.length-2 step 2) {
                elements.add(HexDumpElement(hexString.substring(i, i + 2), byteOffset++))
            }
            return elements
        }
    }
}
