package io.github.binaryfoo.decoders.apdu

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.EmvTags
import io.github.binaryfoo.decoders.DecodeSession
import io.github.binaryfoo.decoders.TLVDecoder
import io.github.binaryfoo.tlv.Tag

import java.util.Arrays
import java.util.Collections

public class ReplyAPDUDecoder(private val tlvDecoder: TLVDecoder) {

    public fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): DecodedData {
        val statusBytesStart = input.length() - 4
        val endIndex: Int
        val children: List<DecodedData>
        val decodedData: String
        if (input.length() == 4) {
            val responseCode = ResponseCode.lookup(input.substring(statusBytesStart))
            decodedData = responseCode.getHex() + " " + responseCode.description
            children = listOf<DecodedData>()
            endIndex = startIndexInBytes + 2
        } else {
            decodedData = input.substring(statusBytesStart)
            children = tlvDecoder.decode(input.substring(0, statusBytesStart), startIndexInBytes, session)
            addToSession(session, children, Arrays.asList(EmvTags.PDOL, EmvTags.CDOL_1, EmvTags.CDOL_2, EmvTags.DDOL))
            val payload = children.get(0)
            endIndex = payload.endIndex + 2
        }
        return DecodedData.constructed("R-APDU", decodedData, startIndexInBytes, endIndex, children)
    }

    private fun addToSession(session: DecodeSession, children: List<DecodedData>, tags: List<Tag>) {
        for (child in children) {
            if (tags.contains(child.tag)) {
                session.put(child.tag, child.fullDecodedData)
            } else if (child.isComposite()) {
                addToSession(session, child.children, tags)
            }
        }
    }
}
