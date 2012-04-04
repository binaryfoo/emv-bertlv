package com.willcurrie.decoders;

import com.willcurrie.DecodedData;
import com.willcurrie.Decoder;
import com.willcurrie.TagInfo;
import com.willcurrie.TagMetaData;
import com.willcurrie.tlv.ISOUtil;
import com.willcurrie.tlv.Tag;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class PopulatedDOLDecoder implements Decoder {
    @Override
    public List<DecodedData> decode(String input, int startIndexInBytes, DecodeSession session) {
        String[] fields = input.split(":");
        String pdol = fields[0];
        String populatedPDOL = fields[1];
        return decode(pdol, populatedPDOL, pdol.length()/2, session);
    }

    public List<DecodedData> decode(String pdol, String populatedPDOL, int startIndexInBytes, DecodeSession session) {
        ArrayList<DecodedData> decoded = new ArrayList<DecodedData>();
        ByteBuffer values = ByteBuffer.wrap(ISOUtil.hex2byte(populatedPDOL));
        List<DOLParser.DOLElement> elements = new DOLParser().parse(ISOUtil.hex2byte(pdol));
        int offset = startIndexInBytes;
        for (DOLParser.DOLElement element : elements) {
            byte[] value = new byte[element.getLength()];
            values.get(value);
            TagMetaData tagMetaData = session.getTagMetaData();
            Tag tag = element.getTag();
            TagInfo tagInfo = tagMetaData.get(tag);
            String valueAsHexString = ISOUtil.hexString(value);
            List<DecodedData> children = tagInfo.getDecoder().decode(valueAsHexString, offset, session);
            DecodedData decodedData = new DecodedData(tag.toString(tagMetaData), tagInfo.decodePrimitiveTlvValue(valueAsHexString), offset, offset + value.length, children);
            decoded.add(decodedData);
            offset += value.length;
        }
        return decoded;
    }

    @Override
    public String validate(String input) {
        String[] fields = input.split(":");
        if (fields.length != 2) {
            return "Put : between the DOL and the populated list";
        }
        return null;
    }

    @Override
    public int getMaxLength() {
        return Integer.MAX_VALUE;
    }
}
