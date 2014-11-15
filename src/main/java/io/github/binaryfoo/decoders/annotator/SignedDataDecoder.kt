package io.github.binaryfoo.decoders.annotator

import io.github.binaryfoo.tlv.BerTlv
import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.decoders.DecodeSession
import io.github.binaryfoo.crypto.RecoveredPublicKeyCertificate
import io.github.binaryfoo.crypto.SignedDataRecoverer
import io.github.binaryfoo.crypto.PublicKeyCertificate
import io.github.binaryfoo.tlv.ISOUtil
import io.github.binaryfoo.hex.HexDumpElement

/**
 * Signed data in EMV is RSA encrypted using the private key. Recovery means decrypt using the public key.
 */
trait SignedDataDecoder {
    fun decodeSignedData(session: DecodeSession, decoded: List<DecodedData>)

    /**
     * Use for static and dynamic data from the chip.
     */
    public fun recoverSignedData(signedStaticData: BerTlv,
                                 decodedSSD: DecodedData,
                                 certificateOfSigner: RecoveredPublicKeyCertificate,
                                 decode: (ByteArray, Int) -> List<DecodedData>) {
        val startIndex = decodedSSD.startIndex + signedStaticData.startIndexOfValue
        val result = recoverText(signedStaticData.valueAsHexString, certificateOfSigner, startIndex, decode)
        updateWithRecoveredData(decodedSSD, result, startIndex)
    }

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
    public fun recoverCertificate(encryptedCertificate: BerTlv,
                                  decodedCertificate: DecodedData,
                                  certificateOfSigner: PublicKeyCertificate,
                                  decode: (ByteArray, Int, Int) -> RecoveredPublicKeyCertificate): RecoveryResult {
        val startIndex = decodedCertificate.startIndex + encryptedCertificate.startIndexOfValue
        val result = recoverCertificate(encryptedCertificate.valueAsHexString, certificateOfSigner, startIndex, decode)
        updateWithRecoveredData(decodedCertificate, result, startIndex)
        return result
    }

    private fun recoverCertificate(signedData: String,
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

    private fun updateWithRecoveredData(source: DecodedData, result: RecoveryResult, startIndex: Int) {
        source.addChildren(result.decoded)
        if (result.recoveredHex != null) {
            source.hexDump = HexDumpElement.splitIntoByteLengthStrings(result.recoveredHex, startIndex)
            source.category = "recovered"
        }
    }

    public data class RecoveryResult(public val text: String, val _decoded: List<DecodedData> = listOf(), public val recoveredHex: String? = null, public val certificate: RecoveredPublicKeyCertificate? = null) {
        public val decoded: List<DecodedData>
        get() {
            return listOf(DecodedData.primitive("", text)) + _decoded
        }
    }

}
