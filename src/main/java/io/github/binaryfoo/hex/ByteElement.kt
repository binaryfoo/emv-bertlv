package io.github.binaryfoo.hex

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder
import org.apache.commons.lang.builder.ToStringBuilder
import org.apache.commons.lang.builder.ToStringStyle

public data class ByteElement(val value: String, val byteOffset: Int) : HexDumpElement {

    override fun isByte(): Boolean {
        return true
    }

}
