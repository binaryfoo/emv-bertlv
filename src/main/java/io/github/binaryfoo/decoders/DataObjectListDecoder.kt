package io.github.binaryfoo.decoders

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.Decoder
import io.github.binaryfoo.tlv.ISOUtil
import io.github.binaryfoo.tlv.Tag
import java.nio.ByteBuffer
import java.util.*

class DataObjectListDecoder : Decoder {
  override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): List<DecodedData> {
    val children = ArrayList<DecodedData>()
    val buffer = ByteBuffer.wrap(ISOUtil.hex2byte(input))
    var offset = startIndexInBytes
    while (buffer.hasRemaining()) {
      val tag = Tag.parse(buffer)
      val b = buffer.get()
      val newOffset = offset + tag.bytes.size + 1
      children.add(DecodedData.primitive("", tag.toString(session.tagMetaData!!) + " " + b + " bytes", offset, newOffset))
      offset = newOffset
    }
    return children
  }

  override fun validate(input: String?): String? {
    if (input != null && input.matches(Regex("^[0-9A-Za-z]$"))) {
      return "Only A-Z and 0-9 are valid"
    }
    return null
  }

  override fun getMaxLength(): Int {
    return Integer.MAX_VALUE
  }
}
