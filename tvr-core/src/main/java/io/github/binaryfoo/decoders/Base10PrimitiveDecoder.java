package io.github.binaryfoo.decoders;

public class Base10PrimitiveDecoder implements PrimitiveDecoder {
    @Override
    public String decode(String hexString) {
        return String.valueOf(Integer.parseInt(hexString, 16));
    }
}
