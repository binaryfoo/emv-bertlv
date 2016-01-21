package io.github.binaryfoo.hex

import java.util.*
import kotlin.text.substring

public data class HexDumpElement(val value: String, val byteOffset: Int) {

    companion object {
        @JvmStatic public fun splitIntoByteLengthStrings(hexString: String, startIndexInBytes: Int): List<HexDumpElement> {
            val elements = ArrayList<HexDumpElement>()
            var byteOffset = startIndexInBytes
            for (i in 0..hexString.length-2 step 2) {
                elements.add(HexDumpElement(hexString.substring(i, i + 2), byteOffset++))
            }
            return elements
        }
    }
}
