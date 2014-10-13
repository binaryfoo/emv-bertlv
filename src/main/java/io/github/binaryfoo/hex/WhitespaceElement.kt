package io.github.binaryfoo.hex

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder
import org.apache.commons.lang.builder.ToStringBuilder
import org.apache.commons.lang.builder.ToStringStyle

public data class WhitespaceElement(val value: String) : HexDumpElement {

    override fun isByte(): Boolean {
        return false
    }
}
