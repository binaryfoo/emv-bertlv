package io.github.binaryfoo.decoders

import io.github.binaryfoo.decoders.annotator.Annotater
import io.github.binaryfoo.EmvTags
import io.github.binaryfoo.tlv.ISOUtil
import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.findForValue
import io.github.binaryfoo.findForTag

/**
 * EMV 4.3 Book 2, Table 7: Format of Data Recovered from Signed Static Application Data
 *
 * Static data auth means CA (scheme) -> Issuer -> data. Chip has no RSA hardware. No replay attack prevention.
 */
public class SignedStaticApplicationDataDecoder : Annotater {

    override fun createNotes(session: DecodeSession, decoded: List<DecodedData>) {
        val issuerKeyExponent = session.findTag(EmvTags.ISSUER_PUBLIC_KEY_EXPONENT)
        val issuerPublicKeyCertificate = session.issuerPublicKeyCertificate
        val signedStaticData = session.findTag(EmvTags.SIGNED_STATIC_APPLICATION_DATA)
        if (issuerKeyExponent != null && signedStaticData != null && issuerPublicKeyCertificate != null) {
            val recovered = SignedDataRecoverer().recover(signedStaticData, issuerKeyExponent, issuerPublicKeyCertificate.fullKey)
            val dump = decode(recovered, issuerPublicKeyCertificate.fullKey.size / 2)
            decoded.findForTag(EmvTags.SIGNED_STATIC_APPLICATION_DATA)!!.notes = dump
        }
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

