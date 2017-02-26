package io.github.binaryfoo.decoders.apdu

import kotlin.collections.firstOrNull

enum class APDUCommand constructor(val firstTwoBytes: String) {
    Select("00A4"),
    ReadRecord("00B2"),
    ReadBinary("00B0"),
    GetProcessingOptions("80A8"),
    GenerateAC("80AE"),
    ComputeCryptographicChecksum("802A"),
    GetData("80CA"),
    ExternalAuthenticate("0082"),
    InternalAuthenticate("0088"),
    Verify("0020"),
    GetChallenge("0084"),
    PutData("04DA");

    companion object {
        fun fromHex(hex: String): APDUCommand? = values().firstOrNull { it.firstTwoBytes == hex }
    }
}
