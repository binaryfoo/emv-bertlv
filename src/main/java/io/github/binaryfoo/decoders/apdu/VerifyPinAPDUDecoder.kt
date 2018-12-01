package io.github.binaryfoo.decoders.apdu

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.decoders.DecodeSession

class VerifyPinAPDUDecoder : CommandAPDUDecoder {
  override fun getCommand(): APDUCommand {
    return APDUCommand.Verify
  }

  override fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): DecodedData {
    val length = Integer.parseInt(input.substring(8, 10), 16)
    val data = input.substring(10, 10 + length * 2)
    return DecodedData(null, "C-APDU: Verify PIN", data, startIndexInBytes, startIndexInBytes + 5 + length)
  }
}
