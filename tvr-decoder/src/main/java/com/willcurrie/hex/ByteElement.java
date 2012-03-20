package com.willcurrie.hex;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ByteElement implements HexDumpElement {

    private final String value;
    private final int byteOffset;

    public ByteElement(String value, int byteOffset) {
        this.value = value;
        this.byteOffset = byteOffset;
    }

    @Override
    public boolean isByte() {
        return true;
    }

    public int getByteOffset() {
        return byteOffset;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
