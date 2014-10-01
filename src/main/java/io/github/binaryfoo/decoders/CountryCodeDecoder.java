package io.github.binaryfoo.decoders;

public class CountryCodeDecoder extends CodeToAlphaDecoder {

    public CountryCodeDecoder() {
        super("numeric-country-list.csv", 3);
    }

}
