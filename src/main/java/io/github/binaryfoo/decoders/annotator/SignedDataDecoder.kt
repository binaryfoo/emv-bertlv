package io.github.binaryfoo.decoders.annotator

import io.github.binaryfoo.tlv.BerTlv
import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.decoders.DecodeSession
import io.github.binaryfoo.crypto.RecoveredPublicKeyCertificate
import io.github.binaryfoo.crypto.SignedDataRecoverer
import io.github.binaryfoo.crypto.PublicKeyCertificate
import io.github.binaryfoo.tlv.ISOUtil
import io.github.binaryfoo.hex.HexDumpElement
import kotlin.collections.listOf
import kotlin.collections.plus

/**
 * Signed data in EMV is RSA encrypted using the private key. Recovery means decrypt using the public key.
 */
interface SignedDataDecoder {
    fun decodeSignedData(session: DecodeSession, decoded: List<DecodedData>)

    /**
     * Use for static and dynamic data from the chip.
     *
     * @param signedData The lump of bytes encrypted using certificateOfSigner.
     * @param decodedSignedData Provides the position of signedData in the stream of data being decoded.
     *                          The output of decode will be added to this element as children.
     * @param certificateOfSigner The RSA public key modulus and exponent used to recover the signedData.
     *                           If recovery works then the holder of the cert encrypted signedData.
     *                           Or the security of the system is broken...
     * @param decode Function to break apart the bytes recovered from signedData.
     */
    fun recoverSignedData(signedData: BerTlv,
                                 decodedSignedData: DecodedData,
                                 certificateOfSigner: RecoveredPublicKeyCertificate,
                                 decode: (ByteArray, Int) -> List<DecodedData>) {
        val startIndex = decodedSignedData.startIndex + signedData.startIndexOfValue
        val result = recoverText(signedData.valueAsHexString, certificateOfSigner, startIndex, decode)
        updateWithRecoveredData(decodedSignedData, result, startIndex)
    }

    fun recoverText(signedData: String,
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
                return RecoveryResult("Failed to recover: $e")
            }
        }
    }

    /**
     * Use for issuer and ICC certificates.
     */
    fun recoverCertificate(encryptedCertificate: BerTlv,
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
                return RecoveryResult("Failed to recover: $e")
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

    data class RecoveryResult(val text: String, val _decoded: List<DecodedData> = listOf(), val recoveredHex: String? = null, val certificate: RecoveredPublicKeyCertificate? = null) {
        val decoded: List<DecodedData>
        get() {
            return listOf(DecodedData.primitive("", text)) + _decoded
        }
    }

}
