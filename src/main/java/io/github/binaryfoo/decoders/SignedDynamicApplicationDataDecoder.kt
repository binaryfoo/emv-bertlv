package io.github.binaryfoo.decoders

import io.github.binaryfoo.tlv.ISOUtil
import io.github.binaryfoo.decoders.annotator.Annotater
import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.EmvTags
import io.github.binaryfoo.findForValue

/**
 * Dynamic data auth means CA (scheme) -> Issuer -> ICC -> data
 */
public class SignedDynamicApplicationDataDecoder : Annotater {
    override fun createNotes(session: DecodeSession, decoded: List<DecodedData>) {
        val iccKeyExponent = session.findTag(EmvTags.ICC_PUBLIC_KEY_EXPONENT)
        val iccPublicKeyCertificate = session.iccPublicKeyCertificate
        val signedData = session.signedDynamicAppData
        if (iccKeyExponent != null && signedData != null && iccPublicKeyCertificate != null) {
            val recovered = SignedDataRecoverer().recover(signedData, iccKeyExponent, iccPublicKeyCertificate.fullKey)
            val dump = decode(recovered, iccPublicKeyCertificate.fullKey.size / 2)
            decoded.findForValue(signedData)!!.notes = dump
        }
    }

    public fun decode(recovered: ByteArray, byteLengthOfICCModulus: Int): String {
        val b = StringBuilder()
        b.append("Header: ").append(ISOUtil.hexString(recovered, 0, 1)).append('\n')
        b.append("Format: ").append(ISOUtil.hexString(recovered, 1, 1)).append('\n')
        b.append("Hash algorithm: ").append(ISOUtil.hexString(recovered, 2, 1)).append('\n')
        val dynamicDataLength = Integer.parseInt(ISOUtil.hexString(recovered, 3, 1), 16)
        b.append("Dynamic data length: ").append(dynamicDataLength).append('\n')
        b.append("Dynamic data: ").append(ISOUtil.hexString(recovered, 4, dynamicDataLength)).append('\n')
        b.append("Hash: ").append(ISOUtil.hexString(recovered, recovered.size - 21, 20)).append('\n')
        b.append("Trailer: ").append(ISOUtil.hexString(recovered, recovered.size - 1, 1)).append('\n')
        return b.toString()
    }
}
