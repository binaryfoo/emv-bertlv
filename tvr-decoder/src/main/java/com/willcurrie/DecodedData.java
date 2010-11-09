package com.willcurrie;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class DecodedData {
	private final String rawData;
	private final String decodedData;
	private final List<DecodedData> children;
	private final int startIndex;
	private final int endIndex;
	
	public DecodedData(String rawData, String decodedData, int startIndex, int endIndex) {
		this(rawData, decodedData, startIndex, endIndex, null);
	}
	
	public DecodedData(String rawData, String decodedData, int startIndex, int endIndex, List<DecodedData> children) {
		this.rawData = rawData;
		this.decodedData = children != null && !children.isEmpty() ? trim(decodedData) : decodedData;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.children = children;
	}
	
	private String trim(String decodedData) {
		return decodedData.length() >= 40 ? decodedData.substring(0, 36) + "..." + StringUtils.right(decodedData, 4) : decodedData;
	}

	public String getRawData() {
		return rawData;
	}

	public int getStartIndex() {
		return startIndex;
	}
	
	public int getEndIndex() {
		return endIndex;
	}
	
	public String getDecodedData() {
		return decodedData;
	}

	public List<DecodedData> getChildren() {
		return children;
	}
	
	public boolean isComposite() {
		return children != null && !children.isEmpty();
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
		String s = String.format("raw=[%s] decoded=[%s] indexes=[%d,%d]", rawData, decodedData, startIndex, endIndex);
		if (isComposite()) {
			for (DecodedData d : children) {
				s += "\n" + d;
			}
		}
		return s;
	}
	
}
