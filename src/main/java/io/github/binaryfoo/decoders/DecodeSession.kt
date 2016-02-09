package io.github.binaryfoo.decoders

import io.github.binaryfoo.TagMetaData
import io.github.binaryfoo.crypto.RecoveredPublicKeyCertificate
import io.github.binaryfoo.decoders.apdu.APDUCommand
import io.github.binaryfoo.tlv.CommonVendorErrorMode
import io.github.binaryfoo.tlv.Tag
import io.github.binaryfoo.tlv.TagRecognitionMode
import java.util.*

class DecodeSession : HashMap<Tag, String>() {

    var firstGenerateACCommand: Boolean = true
    var tagMetaData: TagMetaData? = null
    var currentCommand: APDUCommand? = null
    var issuerPublicKeyCertificate: RecoveredPublicKeyCertificate? = null
    var iccPublicKeyCertificate: RecoveredPublicKeyCertificate? = null
    var signedDynamicAppData: String? = null
    var tagRecognitionMode: TagRecognitionMode = CommonVendorErrorMode

}
