package io.github.binaryfoo;

import io.github.binaryfoo.decoders.DataObjectListDecoder;
import io.github.binaryfoo.decoders.DecodeSession;
import io.github.binaryfoo.decoders.PopulatedDOLDecoder;
import io.github.binaryfoo.decoders.TLVDecoder;
import io.github.binaryfoo.decoders.apdu.*;
import io.github.binaryfoo.tlv.Tag;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The main entry point.
 */
public class RootDecoder {
    private static Map<String, TagMetaData> TAG_META_SETS = new LinkedHashMap<String, TagMetaData>();
    private static Map<String, TagInfo> ROOT_TAG_INFO = new LinkedHashMap<String, TagInfo>();
    static {
        putTag(EmvTags.TERMINAL_VERIFICATION_RESULTS, EmvTags.METADATA);
        putTag(EmvTags.TSI, EmvTags.METADATA);
        putTag(EmvTags.APPLICATION_INTERCHANGE_PROFILE, EmvTags.METADATA);
        putTag(EmvTags.CVM_LIST, EmvTags.METADATA);
        putTag(EmvTags.CVM_RESULTS, EmvTags.METADATA);
        putTag(QVsdcTags.CARD_TX_QUALIFIERS, QVsdcTags.METADATA);
        putTag(QVsdcTags.TERMINAL_TX_QUALIFIERS, QVsdcTags.METADATA);
        ROOT_TAG_INFO.put("dol", new TagInfo("DOL", "Data Object List", new DataObjectListDecoder()));
        ROOT_TAG_INFO.put("filled-dol", new TagInfo("Filled DOL", "Data Object List", new PopulatedDOLDecoder()));
        ROOT_TAG_INFO.put("constructed", new TagInfo("TLV Data", "Constructed TLV data", new TLVDecoder()));
        ROOT_TAG_INFO.put("apdu-sequence", new TagInfo("APDUs", "Sequence of Command/Reply APDUs", new APDUSequenceDecoder(
                new ReplyAPDUDecoder(new TLVDecoder()),
                new SelectCommandAPDUDecoder(),
                new GetProcessingOptionsCommandAPDUDecoder(),
                new ReadRecordAPDUDecoder(),
                new GenerateACAPDUDecoder(),
                new GetDataAPDUDecoder(),
                new ExternalAuthenticateAPDUDecoder(),
                new ComputeCryptoChecksumDecoder(),
                new InternalAuthenticateAPDUDecoder(),
                new VerifyPinAPDUDecoder(),
                new GetChallengeAPDUDecoder()
        )));
    }
    static {
        TAG_META_SETS.put("EMV", EmvTags.METADATA);
        TAG_META_SETS.put("qVSDC", QVsdcTags.METADATA);
        TAG_META_SETS.put("MSD", MSDTags.METADATA);
        TAG_META_SETS.put("Amex", AmexTags.METADATA);
    }

    public RootDecoder() {
    }

    private static void putTag(Tag tag, TagMetaData tagMetaData) {
        ROOT_TAG_INFO.put(tag.getHexString(), tagMetaData.get(tag));
    }

    public static TagInfo getTagInfo(String tag) {
        return ROOT_TAG_INFO.get(tag);
    }

    public static Set<Map.Entry<String, TagInfo>> getSupportedTags() {
        return ROOT_TAG_INFO.entrySet();
    }

    public static Set<String> getAllTagMeta() {
        return TAG_META_SETS.keySet();
    }

    /**
     * f(hex string) -> somewhat english description
     *
     * @param value Hex string to decode.
     * @param meta One of the keys in io.github.binaryfoo.RootDecoder#TAG_META_SETS.
     * @param tagInfo One of the values returned by io.github.binaryfoo.RootDecoder#getTagInfo(java.lang.String).
     *
     * @return Somewhat english description.
     */
    public List<DecodedData> decode(String value, String meta, TagInfo tagInfo) {
        DecodeSession decodeSession = new DecodeSession();
        decodeSession.setTagMetaData(getTagMetaData(meta));
        return tagInfo.getDecoder().decode(value, 0, decodeSession);
    }

    public TagMetaData getTagMetaData(String meta) {
        TagMetaData tagMetaData = TAG_META_SETS.get(meta);
        return tagMetaData == null ? EmvTags.METADATA : tagMetaData;
    }
}