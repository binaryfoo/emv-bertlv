package io.github.binaryfoo.decoders

import io.github.binaryfoo.TagMetaData
import io.github.binaryfoo.decoders.apdu.APDUCommand
import io.github.binaryfoo.tlv.Tag

import java.util.HashMap
import io.github.binaryfoo.tlv.BerTlv
import io.github.binaryfoo.crypto.RecoveredPublicKeyCertificate

public class DecodeSession : HashMap<Tag, String>() {

    private var firstGenerateACCommand = true
    public var tagMetaData: TagMetaData? = null
    public var currentCommand: APDUCommand? = null
    public var issuerPublicKeyCertificate: RecoveredPublicKeyCertificate? = null
    public var iccPublicKeyCertificate: RecoveredPublicKeyCertificate? = null
    public var signedDynamicAppData: String? = null

    public fun isFirstGenerateACCommand(): Boolean {
        return firstGenerateACCommand
    }

    public fun setFirstGenerateACCommand(firstGenerateACCommand: Boolean) {
        this.firstGenerateACCommand = firstGenerateACCommand
    }

    public fun findTag(tag: Tag): String? {
        return this[tag]
    }

    public fun rememberTags(tlvs: List<BerTlv>) {
        tlvs.forEach { this[it.tag] = it.getValueAsHexString() }
    }
}
