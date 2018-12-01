package io.github.binaryfoo;

import io.github.binaryfoo.tlv.Tag;

public class MSDTags extends EmvTags {
  public static final TagMetaData METADATA = EmvTags.METADATA.join(TagMetaData.load("msd.yaml"));

  public static final Tag MSD_TRACK_1 = Tag.fromHex("56");
  public static final Tag MSD_TRACK_2 = Tag.fromHex("9F6B");
  public static final Tag MSD_CVC_3_TRACK_1 = Tag.fromHex("9F60");
  public static final Tag MSD_CVC_3_TRACK_2 = Tag.fromHex("9F61");
  public static final Tag MSD_POSITION_OF_CVC_3_TRACK_1 = Tag.fromHex("9F62");
  public static final Tag MSD_POSITION_OF_CVC_3_TRACK_2 = Tag.fromHex("9F65");
  public static final Tag MSD_POSITION_OF_UN_AND_ATC_TRACK_1 = Tag.fromHex("9F63");
  public static final Tag MSD_POSITION_OF_UN_AND_ATC_TRACK_2 = Tag.fromHex("9F66");
  public static final Tag MSD_NATC_TRACK_1 = Tag.fromHex("9F64");
  public static final Tag MSD_NATC_TRACK_2 = Tag.fromHex("9F67");
  public static final Tag MSD_CVM_LIST = Tag.fromHex("9F68");
  public static final Tag MSD_APPLICATION_VERSION_NUMBER = Tag.fromHex("9F6C");
}
