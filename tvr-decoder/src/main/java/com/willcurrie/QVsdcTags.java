package com.willcurrie;

import com.willcurrie.decoders.CVMListDecoder;
import com.willcurrie.decoders.CardTxQualifiersDecoder;
import com.willcurrie.decoders.PrimitiveDecoder;
import com.willcurrie.decoders.TerminalTxQualifiersDecoder;
import com.willcurrie.tlv.Tag;

import static com.willcurrie.decoders.PrimitiveDecoder.ASCII;
import static com.willcurrie.decoders.PrimitiveDecoder.HEX;

public class QVsdcTags extends EmvTags {
    public static final TagMetaData METADATA = new TagMetaData(EmvTags.METADATA);

    public static final Tag TERMINAL_TX_QUALIFIERS = newTag("9F66", "TTQ", "Terminal transaction qualifiers", new TerminalTxQualifiersDecoder());
    public static final Tag CARD_TX_QUALIFIERS = newTag("9F6C", "CTQ", "Card transaction qualifiers", new CardTxQualifiersDecoder());
    public static final Tag CARD_AUTH_RELATED_DATA = newTag("9F69", "Card Authentication Related Data", HEX);

    private static Tag newTag(String hexString, String shortName, String longName, Decoder decoder) {
        return newTag(METADATA, hexString, shortName, longName, decoder);
    }

    private static Tag newTag(String hexString, String name, PrimitiveDecoder primitiveDecoder) {
        return newTag(METADATA, hexString, name, primitiveDecoder);
    }
}
