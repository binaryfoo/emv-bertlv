package io.github.binaryfoo.decoders;

import io.github.binaryfoo.TagMetaData;
import io.github.binaryfoo.decoders.apdu.APDUCommand;
import io.github.binaryfoo.tlv.Tag;

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
