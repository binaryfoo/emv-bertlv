package io.github.binaryfoo.decoders;

public class NullPrimitiveDecoder implements PrimitiveDecoder {
    @Override
    public String decode(String hexString) {
        return hexString;
    }
}
