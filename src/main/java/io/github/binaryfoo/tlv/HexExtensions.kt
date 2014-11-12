package io.github.binaryfoo.tlv

public fun Iterable<Byte>.toHexString(): String {
    return ISOUtil.hexString(this)
}

public fun ByteArray.toHexString(): String {
    return ISOUtil.hexString(this)
}

public fun Byte.toHexString(): String {
    return ISOUtil.hexString(this)
}

public fun String.decodeAsHex(): ByteArray {
    return ISOUtil.hex2byte(this);
}
