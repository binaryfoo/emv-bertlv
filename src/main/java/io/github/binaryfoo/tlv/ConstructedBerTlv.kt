package io.github.binaryfoo.tlv

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.ArrayList
import java.util.Arrays

class ConstructedBerTlv(tag: Tag, private val children: List<BerTlv>) : BerTlv(tag) {

    override fun getChildren(): List<BerTlv> {
        return children
    }

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

    override fun findTlv(tag: Tag): BerTlv? {
        for (tlv in children) {
            if (tlv.tag == tag) {
                return tlv
            }
        }
        return null
    }

    override fun findTlvs(tag: Tag): List<BerTlv> {
        val matches = ArrayList<BerTlv>()
        for (tlv in children) {
            if (tlv.tag == tag) {
                matches.add(tlv)
            }
        }
        return matches
    }

    override fun toString(): String {
        val buffer = StringBuffer("\nTag: ")
        buffer.append(tag).append('\n')
        for (tlv in children) {
            buffer.append(tlv).append('\n')
        }
        buffer.append("End tag: ").append(tag).append('\n')
        return buffer.toString()
    }
}

