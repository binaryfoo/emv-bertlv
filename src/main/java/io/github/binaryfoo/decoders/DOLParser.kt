package io.github.binaryfoo.decoders

import io.github.binaryfoo.tlv.Tag
import java.nio.ByteBuffer
import java.util.*

/**
 * DOL = Data Object List = A description of some data a receiver would like.
 *
 * Each element in the list is a tag and a length the receiver would like the value encoded on.
 */
class DOLParser {

  fun parse(dol: ByteArray): List<DOLElement> {
    val elements = ArrayList<DOLElement>()
    val buffer = ByteBuffer.wrap(dol)
    while (buffer.hasRemaining()) {
      val tag = Tag.parse(buffer)
      val length = buffer.get()
      elements.add(DOLElement(tag, length.toInt()))
    }
    return elements
  }

  data class DOLElement(val tag: Tag, val length: Int)
}
