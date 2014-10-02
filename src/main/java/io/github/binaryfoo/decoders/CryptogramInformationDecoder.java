package io.github.binaryfoo.decoders;

public class CryptogramInformationDecoder implements PrimitiveDecoder {

    @Override
    public String decode(String hexString) {
        CryptogramType cryptogramType = CryptogramType.fromHex(hexString);
        return cryptogramType == null ? hexString : cryptogramType.toString();
    }
}
