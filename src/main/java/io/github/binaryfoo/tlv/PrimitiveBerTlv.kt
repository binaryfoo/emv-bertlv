package io.github.binaryfoo.tlv

import kotlin.collections.listOf

/**
 * The V is just some bytes.
 */
class PrimitiveBerTlv(tag: Tag, private val value: ByteArray) : BerTlv(tag) {

    override fun getValue(): ByteArray = value

    override fun findTlv(tag: Tag): BerTlv? {
        return null
    }

    override fun findTlvs(tag: Tag): List<BerTlv> {
        return listOf()
    }

    override fun getChildren(): List<BerTlv> {
        return listOf()
    }

    override fun toString(): String = "$tag: ${ISOUtil.hexString(value)}"
}

