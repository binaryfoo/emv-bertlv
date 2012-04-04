package com.willcurrie;

import com.willcurrie.decoders.DataObjectListDecoder;
import com.willcurrie.decoders.DecodeSession;
import com.willcurrie.decoders.PopulatedDOLDecoder;
import com.willcurrie.decoders.TLVDecoder;
import com.willcurrie.decoders.apdu.*;
import com.willcurrie.tlv.Tag;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        ROOT_TAG_INFO.put("apdu-sequence", new TagInfo("APDUs", "Sequence of Command/Reply APDUs", new APDUSequenceDecoder(new ReplyAPDUDecoder(new TLVDecoder()),
                new SelectCommandAPDUDecoder(), new GetProcessingOptionsCommandAPDUDecoder(), new ReadRecordAPDUDecoder(),
                new GenerateACAPDUDecoder(), new GetDataAPDUDecoder(), new ExternalAuthenticateAPDUDecoder(), new ComputeCryptoChecksumDecoder())));
    }
    static {
        TAG_META_SETS.put("EMV", EmvTags.METADATA);
        TAG_META_SETS.put("qVSDC", QVsdcTags.METADATA);
        TAG_META_SETS.put("MSD", MSDTags.METADATA);
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