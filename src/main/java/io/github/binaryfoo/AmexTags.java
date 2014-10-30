package io.github.binaryfoo;

import io.github.binaryfoo.tlv.Tag;

public class AmexTags extends EmvTags {
    public static final TagMetaData METADATA = EmvTags.METADATA.join(TagMetaData.load("amex.yaml"));

    public static final Tag TERMINAL_TX_CAPABILITIES = Tag.fromHex("9F6E");
    public static final Tag EXPRESSPAY_TERMINAL_CAPABILITIES = Tag.fromHex("9F6D");
    public static final Tag CARD_INTERFACE_CAPABILITIES = Tag.fromHex("9F70");
    public static final Tag MOBILE_CVM_RESULTS = Tag.fromHex("9F71");
    public static final Tag CHECK_CURRENCY_CODE = Tag.fromHex("9F66");
}
