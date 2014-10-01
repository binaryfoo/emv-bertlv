package io.github.binaryfoo.decoders;

public interface PrimitiveDecoder {

    PrimitiveDecoder HEX = new NullPrimitiveDecoder();
    PrimitiveDecoder ASCII = new AsciiPrimitiveDecoder();
    PrimitiveDecoder BASE_10 = new Base10PrimitiveDecoder();
    PrimitiveDecoder CURRENCY_CODE = new CurrencyCodeDecoder();
    PrimitiveDecoder COUNTRY_CODE = new CountryCodeDecoder();

    String decode(String hexString);
}
