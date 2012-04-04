package com.willcurrie.tlv;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ConstructedBerTlv extends BerTlv {

    private final List<BerTlv> data;

    ConstructedBerTlv(Tag tag, List<BerTlv> data) {
        super(tag);
        this.data = data;
    }

    public byte[] getValue() {
        ByteArrayOutputStream value = new ByteArrayOutputStream();
        for (BerTlv tlv : data) {
            try {
				value.write(tlv.toBinary());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
        }
        return value.toByteArray();
    }

    public BerTlv findTlv(Tag tag) {
    	for (BerTlv tlv : data) {
    		if (Arrays.equals(tlv.getTag().getBytes(), tag.getBytes())) {
                return tlv;
            }
        }
        return null;
    }

    public List<BerTlv> findTlvs(Tag tag) {
        List<BerTlv> matches = new ArrayList<BerTlv>();
        for (BerTlv tlv : data) {
            if (Arrays.equals(tlv.getTag().getBytes(), tag.getBytes())) {
                matches.add(tlv);
            }
        }
        return matches;
    }

    public List<BerTlv> getChildren() {
        return data;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer("\nTag: ");
        buffer.append(getTag()).append('\n');
        for (BerTlv tlv : data) {
        	buffer.append(tlv).append('\n');
		}
        buffer.append("End tag: ").append(getTag()).append('\n');
        return buffer.toString();
    }
}

