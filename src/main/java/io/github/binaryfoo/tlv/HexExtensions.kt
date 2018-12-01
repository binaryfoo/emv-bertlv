package io.github.binaryfoo.tlv

fun Iterable<Byte>.toHexString(): String {
  return ISOUtil.hexString(this)
}

fun ByteArray.toHexString(): String {
  return ISOUtil.hexString(this)
}

fun Byte.toHexString(): String {
  return ISOUtil.hexString(this)
}

fun String.decodeAsHex(): ByteArray {
  return ISOUtil.hex2byte(this)
}
