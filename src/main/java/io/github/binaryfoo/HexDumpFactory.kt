package io.github.binaryfoo

import io.github.binaryfoo.hex.ByteElement
import io.github.binaryfoo.hex.HexDumpElement
import io.github.binaryfoo.hex.WhitespaceElement

import java.util.ArrayList

public class HexDumpFactory {
    public fun splitIntoByteLengthStrings(hexString: String, startIndexInBytes: Int): List<HexDumpElement> {
        val elements = ArrayList<HexDumpElement>()
        var byteOffset = startIndexInBytes
        for (i in 0..hexString.length-2 step 2) {
            elements.add(ByteElement(hexString.substring(i, i + 2), byteOffset++))
        }
        return elements
    }
}