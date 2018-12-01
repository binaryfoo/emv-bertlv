package io.github.binaryfoo.decoders

class CurrencyCodeDecoder : PrimitiveDecoder {

  override fun decode(hexString: String): String {
    return numericToAlpha[hexString.substring(1)] ?: "Unknown"
  }

  companion object {

    private val numericToAlpha: Map<String, String> by lazy {
      csvToMap("numeric-currency-list.csv", 3)
    }
  }

}
