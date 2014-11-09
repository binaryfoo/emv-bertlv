package io.github.binaryfoo.decoders.apdu

public enum class APDUCommand private(public val firstTwoBytes: String) {
    Select : APDUCommand("00A4")
    ReadRecord : APDUCommand("00B2")
    GetProcessingOptions : APDUCommand("80A8")
    GenerateAC : APDUCommand("80AE")
    ComputeCryptographicChecksum : APDUCommand("802A")
    GetData : APDUCommand("80CA")
    ExternalAuthenticate : APDUCommand("0082")
    InternalAuthenticate : APDUCommand("0088")
    Verify : APDUCommand("0020")
    GetChallenge : APDUCommand("0084")
    PutData : APDUCommand("04DA")

    class object {
        public fun fromHex(hex: String): APDUCommand? = values().firstOrNull { it.firstTwoBytes == hex }
    }
}
