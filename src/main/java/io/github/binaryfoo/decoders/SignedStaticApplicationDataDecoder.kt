package io.github.binaryfoo.decoders

import io.github.binaryfoo.decoders.annotator.Annotater
import io.github.binaryfoo.EmvTags
import io.github.binaryfoo.tlv.ISOUtil

/**
 * EMV 4.3 Book 2, Table 7: Format of Data Recovered from Signed Static Application Data
 */
public class SignedStaticApplicationDataDecoder : Annotater {
    override fun createNotes(session: DecodeSession): String? {
        val issuerKeyExponent = session.findTag(EmvTags.ISSUER_PUBLIC_KEY_EXPONENT)
        val signedStaticData = session.findTag(EmvTags.SIGNED_STATIC_APPLICATION_DATA)
        val issuerPublicKeyCertificate = session.issuerPublicKeyCertificate
        if (issuerKeyExponent != null && signedStaticData != null && issuerPublicKeyCertificate != null) {
            val recovered = SignedDataRecoverer().recover(signedStaticData, issuerKeyExponent, issuerPublicKeyCertificate.fullKey)
            return decode(recovered, issuerPublicKeyCertificate.fullKey.size/2)
        }
        return null
    }

    fun decode(recovered: ByteArray, byteLengthOfIssuerModulus: Int): String {
        val header = ISOUtil.hexString(recovered, 0, 1)
        val format = ISOUtil.hexString(recovered, 1, 1)
        val hashAlgorithm = ISOUtil.hexString(recovered, 2, 1)
        val dataAuthenticationCode = ISOUtil.hexString(recovered, 3, 2)
        val hash = ISOUtil.hexString(recovered, recovered.size-21, 20)
        val trailer = ISOUtil.hexString(recovered, recovered.size-1, 1)
        return "Header: ${header}\n" +
                "Format: ${format}\n" +
                "Hash Algorithm: ${hashAlgorithm}\n" +
                "Data Auth Code: ${dataAuthenticationCode}\n" +
                "Hash: ${hash}\n" +
                "Trailer: ${trailer}\n"
    }
}

