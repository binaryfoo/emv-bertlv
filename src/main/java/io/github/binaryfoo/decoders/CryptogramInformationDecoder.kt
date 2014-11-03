package io.github.binaryfoo.decoders

public class CryptogramInformationDecoder : PrimitiveDecoder {

    override fun decode(hexString: String): String {
        val cryptogramType = CryptogramType.fromHex(hexString)
        return if (cryptogramType == null) hexString else cryptogramType.toString()
    }
}
