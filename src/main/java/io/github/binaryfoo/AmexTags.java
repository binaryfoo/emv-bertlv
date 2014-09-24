package io.github.binaryfoo;

import io.github.binaryfoo.decoders.*;
import io.github.binaryfoo.tlv.Tag;

public class AmexTags extends EmvTags {
    public static final TagMetaData METADATA = new TagMetaData(EmvTags.METADATA);

    public static final Tag TERMINAL_TX_CAPABILITIES = newTag("9F6E", "Terminal transaction capabilities", new AmexTerminalCapabilitiesDecoder());
    public static final Tag EXPRESSPAY_TERMINAL_CAPABILITIES = newTag("9F6D", "XP Terminal capabilities", new AmexTerminalTxCapabilitiesDecoder());
    public static final Tag CARD_INTERFACE_CAPABILITIES = newTag("9F70", "Card interface capabilities", new AmexCardInterfaceCapabilitiesDecoder());
    public static final Tag MOBILE_CVM_RESULTS = newTag("9F71", "Mobile CVM Results", new MobileCVMDecoder());
    public static final Tag CHECK_CURRENCY_CODE = newTag("9F66", "Single Transaction Value Limit Check – Dual Currency Code", PrimitiveDecoder.CURRENCY_CODE);

    private static Tag newTag(String hexString, String longName, Decoder decoder) {
        return newTag(METADATA, hexString, longName, longName, decoder);
    }

    private static Tag newTag(String hexString, String name, PrimitiveDecoder primitiveDecoder) {
        return newTag(METADATA, hexString, name, primitiveDecoder);
    }
}
