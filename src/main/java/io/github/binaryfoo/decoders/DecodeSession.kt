package io.github.binaryfoo.decoders

import io.github.binaryfoo.TagMetaData
import io.github.binaryfoo.decoders.apdu.APDUCommand
import io.github.binaryfoo.tlv.Tag

import java.util.HashMap
import io.github.binaryfoo.tlv.BerTlv
import io.github.binaryfoo.crypto.RecoveredPublicKeyCertificate
import java.util.LinkedHashMap
import io.github.binaryfoo.tlv.TagRecognitionMode
import io.github.binaryfoo.tlv.CommonVendorErrorMode

public class DecodeSession : HashMap<Tag, String>() {

    public var firstGenerateACCommand: Boolean = true
    public var tagMetaData: TagMetaData? = null
    public var currentCommand: APDUCommand? = null
    public var issuerPublicKeyCertificate: RecoveredPublicKeyCertificate? = null
    public var iccPublicKeyCertificate: RecoveredPublicKeyCertificate? = null
    public var signedDynamicAppData: String? = null
    public var tagRecognitionMode: TagRecognitionMode = CommonVendorErrorMode

}
