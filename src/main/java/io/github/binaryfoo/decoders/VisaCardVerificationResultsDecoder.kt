package io.github.binaryfoo.decoders

/**
 * From Visa Contactless Payment Specification v2.1 pD-26
 *      Visa Integrated Circuit Card Card Specification, Version 1.4.0 pA-12
 */
public class VisaCardVerificationResultsDecoder : EmvBitStringDecoder("fields/visa-cvr.txt", false)
