package com.willcurrie.decoders;

import com.willcurrie.tlv.Tag;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DecodeSession extends HashMap<Tag, String> {

    private boolean firstGenerateACCommand = true;

    public void setTagsToTreatAsPrimitive(List<Tag> tagsToTreatAsPrimitive) {
    }

    public boolean isFirstGenerateACCommand() {
        return firstGenerateACCommand;
    }

    public void setFirstGenerateACCommand(boolean firstGenerateACCommand) {
        this.firstGenerateACCommand = firstGenerateACCommand;
    }
}
