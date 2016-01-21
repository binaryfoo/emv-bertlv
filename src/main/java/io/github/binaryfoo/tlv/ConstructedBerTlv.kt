package io.github.binaryfoo.tlv

import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * The V represents a set of TLVs.
 */
class ConstructedBerTlv(tag: Tag, private val children: List<BerTlv>) : BerTlv(tag) {

    override fun getChildren(): List<BerTlv> = children

    override fun getValue(): ByteArray {
        val value = ByteArrayOutputStream()
        for (tlv in children) {
            try {
                value.write(tlv.toBinary())
            } catch (e: IOException) {
                throw RuntimeException(e)
            }

        }
        return value.toByteArray()
    }

    override fun findTlv(tag: Tag): BerTlv? = children.firstOrNull { it.tag == tag }

    override fun findTlvs(tag: Tag): List<BerTlv> = children.filter { it.tag == tag }

    override fun toString(): String {
        val buffer = StringBuilder("\nTag: $tag\n")
        for (tlv in children) {
            buffer.append(tlv).append('\n')
        }
        buffer.append("End tag: $tag\n")
        return buffer.toString()
    }
}

