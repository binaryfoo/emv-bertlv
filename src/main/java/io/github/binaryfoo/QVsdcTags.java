package io.github.binaryfoo;

import io.github.binaryfoo.decoders.CardTxQualifiersDecoder;
import io.github.binaryfoo.decoders.PrimitiveDecoder;
import io.github.binaryfoo.decoders.TerminalTxQualifiersDecoder;
import io.github.binaryfoo.tlv.Tag;

import static io.github.binaryfoo.decoders.PrimitiveDecoder.HEX;

public class QVsdcTags extends EmvTags {
    public static final TagMetaData METADATA = TagMetaData.copy(EmvTags.METADATA);

    public static final Tag TERMINAL_TX_QUALIFIERS = newTag("9F66", "TTQ", "Terminal transaction qualifiers", new TerminalTxQualifiersDecoder());
    public static final Tag CARD_TX_QUALIFIERS = newTag("9F6C", "CTQ", "Card transaction qualifiers", new CardTxQualifiersDecoder());
    public static final Tag CARD_AUTH_RELATED_DATA = newTag("9F69", "Card Authentication Related Data", HEX);

    private static Tag newTag(String hexString, String shortName, String longName, Decoder decoder) {
        return METADATA.newTag(hexString, shortName, longName, decoder);
    }

    private static Tag newTag(String hexString, String name, PrimitiveDecoder primitiveDecoder) {
        return METADATA.newTag(hexString, name, name, primitiveDecoder);
    }
}
