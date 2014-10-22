package io.github.binaryfoo.decoders.annotator

import io.github.binaryfoo.tlv.BerTlv
import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.decoders.DecodeSession

trait Annotater {
    fun createNotes(session: DecodeSession, decoded: List<DecodedData>)
}
