package com.willcurrie;

import com.willcurrie.decoders.CVMListDecoder;
import com.willcurrie.decoders.PrimitiveDecoder;
import com.willcurrie.tlv.Tag;

import static com.willcurrie.decoders.PrimitiveDecoder.ASCII;
import static com.willcurrie.decoders.PrimitiveDecoder.HEX;

public class MSDTags extends EmvTags {
    public static final TagMetaData METADATA = new TagMetaData(EmvTags.METADATA);

    public static final Tag MSD_TRACK_1 = newTag("56", "track 1", ASCII);
    public static final Tag MSD_TRACK_2 = newTag("9F6B", "track 2", HEX);
    public static final Tag MSD_CVC_3_TRACK_1 = newTag("9F60", "CVC3 track 1", HEX);
    public static final Tag MSD_CVC_3_TRACK_2 = newTag("9F61", "CVC3 track 2", HEX);
    public static final Tag MSD_POSITION_OF_CVC_3_TRACK_1 = newTag("9F62", "position of CVC3 in track 1", HEX);
    public static final Tag MSD_POSITION_OF_CVC_3_TRACK_2 = newTag("9F65", "position of CVC3 in track 2", HEX);
    public static final Tag MSD_POSITION_OF_UN_AND_ATC_TRACK_1 = newTag("9F63", "position of UN and ATC in track 1", HEX);
    public static final Tag MSD_POSITION_OF_UN_AND_ATC_TRACK_2 = newTag("9F66", "position of UN and ATC in track 2", HEX);
    public static final Tag MSD_NATC_TRACK_1 = newTag("9F64", "number of digits from ATC in track 1", HEX);
    public static final Tag MSD_NATC_TRACK_2 = newTag("9F67", "number of digits from ATC in track 2", HEX);
    public static final Tag MSD_CVM_LIST = newTag("9F68", "CVM List", "Cardholder Verification Method List", new CVMListDecoder());
    public static final Tag MSD_APPLICATION_VERSION_NUMBER = newTag("9F6C", "application version number", HEX);

    private static Tag newTag(String hexString, String shortName, String longName, Decoder decoder) {
        return newTag(METADATA, hexString, shortName, longName, decoder);
    }

    private static Tag newTag(String hexString, String name, PrimitiveDecoder primitiveDecoder) {
        return newTag(METADATA, hexString, name, primitiveDecoder);
    }
}
