package com.willcurrie;

import com.willcurrie.decoders.AsciiPrimitiveDecoder;
import com.willcurrie.decoders.NullDecoder;
import com.willcurrie.decoders.NullPrimitiveDecoder;
import com.willcurrie.tlv.Tag;

import java.util.HashMap;
import java.util.Map;

public class TagMetaData {

    private Map<String, TagInfo> metadata = new HashMap<String, TagInfo>();

    public void put(Tag tag, TagInfo tagInfo) {
        metadata.put(tag.getHexString(), tagInfo);
    }
    
    public TagInfo get(Tag tag) {
        TagInfo tagInfo = metadata.get(tag.getHexString());
        if (tagInfo == null) {
            return new TagInfo("?", "?", new NullDecoder(), new NullPrimitiveDecoder());
        }
        return tagInfo;
    }
}
