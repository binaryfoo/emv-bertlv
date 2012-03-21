package com.willcurrie;

import com.willcurrie.decoders.*;
import com.willcurrie.tlv.Tag;

import java.util.HashMap;
import java.util.Map;

public class TagMetaData {

    private final Map<String, TagInfo> metadata = new HashMap<String, TagInfo>();

    public TagMetaData() {
    }

    public TagMetaData(TagMetaData metadata) {
        this.metadata.putAll(metadata.metadata);
    }

    public void put(Tag tag, TagInfo tagInfo) {
        if (metadata.put(tag.getHexString(), tagInfo) != null) {
            throw new IllegalArgumentException("Duplicate entry for " + tag.getHexString());
        }
    }
    
    public TagInfo get(Tag tag) {
        TagInfo tagInfo = metadata.get(tag.getHexString());
        if (tagInfo == null) {
            return new TagInfo("?", "?", Decoders.PRIMITIVE, PrimitiveDecoder.HEX);
        }
        return tagInfo;
    }
}
