package io.github.binaryfoo.decoders;

public interface PrimitiveDecoder {

    PrimitiveDecoder HEX = new NullPrimitiveDecoder();
    PrimitiveDecoder ASCII = new AsciiPrimitiveDecoder();
    PrimitiveDecoder SOMETIMES_ASCII = new SometimesAsciiPrimitiveDecoder();
    PrimitiveDecoder BASE_10 = new Base10PrimitiveDecoder();
    PrimitiveDecoder CURRENCY_CODE = new CurrencyCodeDecoder();

    String decode(String hexString);
}
