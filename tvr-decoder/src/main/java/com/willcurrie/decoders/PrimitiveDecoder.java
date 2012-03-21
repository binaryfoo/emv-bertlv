package com.willcurrie.decoders;

public interface PrimitiveDecoder {

    PrimitiveDecoder HEX = new NullPrimitiveDecoder();
    PrimitiveDecoder ASCII = new AsciiPrimitiveDecoder();

    String decode(String hexString);
}
