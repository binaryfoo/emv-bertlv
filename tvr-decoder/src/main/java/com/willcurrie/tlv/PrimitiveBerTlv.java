package com.willcurrie.tlv;

import java.util.Collections;
import java.util.List;

import com.willcurrie.EmvTags;

class PrimitiveBerTlv extends BerTlv {

    private final byte[] value;

    PrimitiveBerTlv(Tag tag, byte[] value) {
        super(tag);
        this.value = value;
    }

    public byte[] getValue() {
        return value;
    }

    public BerTlv findTlv(Tag tag) {
        return null;
    }

    public List<BerTlv> findTlvs(Tag tag) {
        return Collections.emptyList();
    }

    public List<BerTlv> getChildren() {
        return Collections.emptyList();
    }

    public String toString() {
        if (EmvTags.isTagAscii(getTag())) {
            return getTag() + ": " + new String(value);
        }
        return getTag() + ": " + ISOUtil.hexString(value);
    }
}

