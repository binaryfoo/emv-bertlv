package io.github.binaryfoo.decoders

// Table 12: Mobile CVM Results (Tag 9F71) Expresspay 3.0
class MobileCVMDecoder : EmvBitStringDecoder("fields/amex-mobile-cvm.txt", false)
