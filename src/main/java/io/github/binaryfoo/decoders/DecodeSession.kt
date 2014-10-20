package io.github.binaryfoo.decoders

import io.github.binaryfoo.TagMetaData
import io.github.binaryfoo.decoders.apdu.APDUCommand
import io.github.binaryfoo.tlv.Tag

import java.util.HashMap

public class DecodeSession : HashMap<Tag, String>() {

    private var firstGenerateACCommand = true
    public var tagMetaData: TagMetaData? = null
    public var currentCommand: APDUCommand? = null

    public fun isFirstGenerateACCommand(): Boolean {
        return firstGenerateACCommand
    }

    public fun setFirstGenerateACCommand(firstGenerateACCommand: Boolean) {
        this.firstGenerateACCommand = firstGenerateACCommand
    }
}
