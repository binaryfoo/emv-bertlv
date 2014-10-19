package io.github.binaryfoo.tlv

import java.util.Collections

class PrimitiveBerTlv(tag: Tag, private val value: ByteArray) : BerTlv(tag) {

    override fun getValue(): ByteArray {
        return value
    }

    override fun findTlv(tag: Tag): BerTlv? {
        return null
    }

    override fun findTlvs(tag: Tag): List<BerTlv> {
        return listOf()
    }

    override fun getChildren(): List<BerTlv> {
        return listOf()
    }

    override fun toString(): String {
        return tag.toString() + ": " + ISOUtil.hexString(value)
    }
}

