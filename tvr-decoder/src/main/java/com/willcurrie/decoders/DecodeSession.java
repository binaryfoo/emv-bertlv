package com.willcurrie.decoders;

import com.willcurrie.TagMetaData;
import com.willcurrie.tlv.Tag;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DecodeSession extends HashMap<Tag, String> {

    private boolean firstGenerateACCommand = true;
    private TagMetaData tagMetaData;

    public boolean isFirstGenerateACCommand() {
        return firstGenerateACCommand;
    }

    public void setFirstGenerateACCommand(boolean firstGenerateACCommand) {
        this.firstGenerateACCommand = firstGenerateACCommand;
    }

    public TagMetaData getTagMetaData() {
        return tagMetaData;
    }

    public void setTagMetaData(TagMetaData tagMetaData) {
        this.tagMetaData = tagMetaData;
    }
}
