package io.github.binaryfoo.decoders.apdu

import io.github.binaryfoo.DecodedData
import io.github.binaryfoo.decoders.DecodeSession

interface CommandAPDUDecoder {
  fun getCommand(): APDUCommand

  fun decode(input: String, startIndexInBytes: Int, session: DecodeSession): DecodedData
}
