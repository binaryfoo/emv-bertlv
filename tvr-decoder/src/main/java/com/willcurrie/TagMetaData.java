package com.willcurrie;

import com.willcurrie.decoders.AsciiPrimitiveDecoder;
import com.willcurrie.decoders.NullDecoder;
import com.willcurrie.decoders.NullPrimitiveDecoder;
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
            return new TagInfo("?", "?", new NullDecoder(), new NullPrimitiveDecoder());
        }
        return tagInfo;
    }
}
