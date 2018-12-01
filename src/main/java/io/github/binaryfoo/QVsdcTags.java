package io.github.binaryfoo;

import io.github.binaryfoo.tlv.Tag;

public class QVsdcTags extends EmvTags {
  public static final TagMetaData METADATA = EmvTags.METADATA.join(TagMetaData.load("qvsdc.yaml"));

  public static final Tag TERMINAL_TX_QUALIFIERS = Tag.fromHex("9F66");
  public static final Tag CARD_TX_QUALIFIERS = Tag.fromHex("9F6C");
  public static final Tag CARD_AUTH_RELATED_DATA = Tag.fromHex("9F69");
}
