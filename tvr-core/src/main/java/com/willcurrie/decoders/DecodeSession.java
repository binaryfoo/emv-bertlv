package com.willcurrie.decoders;

import com.willcurrie.TagMetaData;
import com.willcurrie.decoders.apdu.APDUCommand;
import com.willcurrie.tlv.Tag;

import java.util.HashMap;

public class DecodeSession extends HashMap<Tag, String> {

    private boolean firstGenerateACCommand = true;
    private TagMetaData tagMetaData;
    private APDUCommand command;

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

    public void setCurrentCommand(APDUCommand command) {
        this.command = command;
    }

    public APDUCommand getCommand() {
        return command;
    }
}
