package com.willcurrie.decoders.apdu;

import com.willcurrie.DecodedData;
import com.willcurrie.EmvTags;
import com.willcurrie.decoders.DecodeSession;
import com.willcurrie.decoders.TLVDecoder;
import com.willcurrie.tlv.Tag;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReplyAPDUDecoder {
    private TLVDecoder tlvDecoder;

    public ReplyAPDUDecoder(TLVDecoder tlvDecoder) {
        this.tlvDecoder = tlvDecoder;
    }

    public DecodedData decode(String input, int startIndexInBytes, DecodeSession session) {
        int statusBytesStart = input.length() - 4;
        int endIndex;
        List<DecodedData> children;
        if (input.length() == 4) {
            children = Collections.emptyList();
            endIndex = startIndexInBytes + 2;
        } else {
            children = tlvDecoder.decode(input.substring(0, statusBytesStart), startIndexInBytes, session);
            addToSession(session, children, Arrays.asList(EmvTags.PDOL, EmvTags.CDOL_1, EmvTags.CDOL_2));
            DecodedData payload = children.get(0);
            endIndex = payload.getEndIndex() + 2;
        }
        return new DecodedData("R-APDU", input.substring(statusBytesStart), startIndexInBytes, endIndex, children);
    }

    private void addToSession(DecodeSession session, List<DecodedData> children, List<Tag> tags) {
        for (DecodedData child : children) {
            if (tags.contains(child.getTag())) {
                session.put(child.getTag(), child.getDecodedData());
            } else if (child.isComposite()) {
                addToSession(session, child.getChildren(), tags);
            }
        }
    }
}
