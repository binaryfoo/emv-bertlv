package io.github.binaryfoo.decoders;

public class CurrencyCodeDecoder extends CodeToAlphaDecoder {

    public CurrencyCodeDecoder() {
        super("numeric-currency-list.csv", 3);
    }
}
