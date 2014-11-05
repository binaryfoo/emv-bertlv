package io.github.binaryfoo.decoders.annotator

import io.github.binaryfoo.tlv.BerTlv
import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.decoders.DecodeSession
import io.github.binaryfoo.crypto.RecoveredPublicKeyCertificate
import io.github.binaryfoo.decoders.SignedDataRecoverer
import io.github.binaryfoo.crypto.PublicKeyCertificate
import io.github.binaryfoo.tlv.ISOUtil

/**
 * Signed data in EMV is RSA encrypted using the private key. Recovery means decrypt using the public key.
 */
trait SignedDataDecoder {
    fun createNotes(session: DecodeSession, decoded: List<DecodedData>)

    /**
     * Use for static and dynamic data from the chip.
     */
    public fun recoverText(signedData: String,
                       certificateOfSigner: RecoveredPublicKeyCertificate,
                       startIndexInBytes: Int,
                       decode: (ByteArray, Int) -> List<DecodedData>): RecoveryResult {
        if (certificateOfSigner.exponent == null) {
            return RecoveryResult("Failed to recover: missing ${certificateOfSigner.name} exponent")
        } else {
            try {
                val recoveredBytes: ByteArray = SignedDataRecoverer().recover(signedData, certificateOfSigner.exponent!!, certificateOfSigner.modulus)
                val recoveredData = decode(recoveredBytes, startIndexInBytes)
                return RecoveryResult("Recovered using ${certificateOfSigner.name}", recoveredData, ISOUtil.hexString(recoveredBytes))
            } catch(e: Exception) {
                return RecoveryResult("Failed to recover: ${e}")
            }
        }
    }

    /**
     * Use for issuer and ICC certificates.
     */
    public fun recoverCertificate(signedData: String,
                       certificateOfSigner: PublicKeyCertificate,
                       startIndexInBytes: Int,
                       decode: (ByteArray, Int, Int) -> RecoveredPublicKeyCertificate): RecoveryResult {
        if (certificateOfSigner.exponent == null) {
            return RecoveryResult("Failed to recover: missing ${certificateOfSigner.name} exponent")
        } else {
            try {
                val recoveredBytes: ByteArray = SignedDataRecoverer().recover(signedData, certificateOfSigner.exponent!!, certificateOfSigner.modulus)
                val recoveredCertificate = decode(recoveredBytes, certificateOfSigner.modulusLength, startIndexInBytes)
                return RecoveryResult("Recovered using ${certificateOfSigner.name}", recoveredCertificate.detail, ISOUtil.hexString(recoveredBytes), recoveredCertificate)
            } catch(e: Exception) {
                return RecoveryResult("Failed to recover: ${e}")
            }
        }
    }

    public data class RecoveryResult(public val text: String, val _decoded: List<DecodedData> = listOf(), public val recoveredHex: String? = null, public val certificate: RecoveredPublicKeyCertificate? = null) {
        public val decoded: List<DecodedData>
        get() {
            return listOf(DecodedData.primitive("", text)) + _decoded
        }
    }

}
