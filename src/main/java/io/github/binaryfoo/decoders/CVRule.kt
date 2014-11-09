package io.github.binaryfoo.decoders

public class CVRule(hexString: String) {
    private val failIfUnsuccessful: Boolean
    private val verificationMethod: CardholderVerificationMethod?
    private val conditionCode: CardholderVerificationConditionCode?

    {
        val leftByte = Integer.parseInt(hexString.substring(0, 2), 16)
        val rightByte = Integer.parseInt(hexString.substring(2, 4), 16)
        failIfUnsuccessful = (leftByte and 0x40) == 0
        verificationMethod = CardholderVerificationMethod.fromCode(leftByte and 0x3F)
        conditionCode = CardholderVerificationConditionCode.fromCode(rightByte)
    }

    public fun getDescription(x: Int, y: Int): String {
        var baseRule = "$verificationMethodDescription, $conditionCodeDescription, ${if (failIfUnsuccessful) "FAIL" else "next"}"
        if (conditionCode == CardholderVerificationConditionCode.TxLessThanX || conditionCode == CardholderVerificationConditionCode.TxMoreThanX) {
            baseRule += " (x = $x)"
        } else if (conditionCode == CardholderVerificationConditionCode.TxLessThanY || conditionCode == CardholderVerificationConditionCode.TxMoreThanY) {
            baseRule += " (y = $y)"
        }
        return baseRule
    }

    public val verificationMethodDescription: String = if (verificationMethod == null) "Unknown" else verificationMethod.description

    public val conditionCodeDescription: String = if (conditionCode == null) "Unknown" else conditionCode.description

}