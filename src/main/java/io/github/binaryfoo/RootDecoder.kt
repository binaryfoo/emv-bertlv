package io.github.binaryfoo

import io.github.binaryfoo.decoders.*
import io.github.binaryfoo.decoders.apdu.*
import io.github.binaryfoo.tlv.Tag

import java.util.LinkedHashMap
import kotlin.platform.platformStatic
import java.util.ArrayList

/**
 * The main entry point.
 */
public class RootDecoder {

    /**
     * f(hex string) -> somewhat english description
     *
     * @param value Hex string to decode.
     * @param meta One of the keys in io.github.binaryfoo.RootDecoder#TAG_META_SETS.
     * @param tagInfo One of the values returned by io.github.binaryfoo.RootDecoder#getTagInfo(java.lang.String).
     *
     * @return Somewhat english description.
     */
    public fun decode(value: String, meta: String, tagInfo: TagInfo): List<DecodedData> {
        val decodeSession = DecodeSession()
        decodeSession.tagMetaData = getTagMetaData(meta)
        return tagInfo.decoder.decode(value, 0, decodeSession)
    }

    public fun decode(value: String, meta: String, tag: String): List<DecodedData> {
        return decode(value, meta, getTagInfo(tag)!!)
    }

    public fun getTagMetaData(meta: String): TagMetaData {
        return TAG_META_SETS.get(meta) ?: EmvTags.METADATA
    }

    class object {
        private val TAG_META_SETS = linkedMapOf(
            "EMV" to EmvTags.METADATA,
            "qVSDC" to QVsdcTags.METADATA,
            "MSD" to MSDTags.METADATA,
            "Amex" to AmexTags.METADATA
        )
        private val ROOT_TAG_INFO = linkedMapOf(
            EmvTags.TERMINAL_VERIFICATION_RESULTS to EmvTags.METADATA,
            EmvTags.TSI to EmvTags.METADATA,
            EmvTags.APPLICATION_INTERCHANGE_PROFILE to EmvTags.METADATA,
            EmvTags.CVM_LIST to EmvTags.METADATA,
            EmvTags.CVM_RESULTS to EmvTags.METADATA,
            QVsdcTags.CARD_TX_QUALIFIERS to QVsdcTags.METADATA,
            QVsdcTags.TERMINAL_TX_QUALIFIERS to QVsdcTags.METADATA,
            "dol" to TagInfo.treeStructured("DOL", "Data Object List", DataObjectListDecoder()),
            "filled-dol" to TagInfo.treeStructured("Filled DOL", "Data Object List", PopulatedDOLDecoder()),
            "constructed" to TagInfo.treeStructured("TLV Data", "Constructed TLV data", TLVDecoder()),
            "apdu-sequence" to TagInfo.treeStructured("APDUs", "Sequence of Command/Reply APDUs", APDUSequenceDecoder(ReplyAPDUDecoder(TLVDecoder()), SelectCommandAPDUDecoder(), GetProcessingOptionsCommandAPDUDecoder(), ReadRecordAPDUDecoder(), GenerateACAPDUDecoder(), GetDataAPDUDecoder(), ExternalAuthenticateAPDUDecoder(), ComputeCryptoChecksumDecoder(), InternalAuthenticateAPDUDecoder(), VerifyPinAPDUDecoder(), GetChallengeAPDUDecoder(), PutDataAPDUDecoder())),
            "bit-string" to TagInfo.treeStructured("Bits", "EMV Bit String", ByteLabeller())
        )

        private fun Tag.to(that: TagMetaData): Pair<String, TagInfo> = Pair(this.getHexString(), that.get(this))

        platformStatic public fun getTagInfo(tag: String): TagInfo? {
            return ROOT_TAG_INFO.get(tag)
        }

        // array because List<TagInfo> and friends seem to show up in .java as List<Object>
        // at least when using emv-bertlv as a library
        platformStatic public fun getSupportedTags(): Array<Map.Entry<String, TagInfo>> {
            return ROOT_TAG_INFO.entrySet().copyToArray()
        }

        platformStatic public fun getAllTagMeta(): Set<String> {
            return TAG_META_SETS.keySet()
        }
    }
}