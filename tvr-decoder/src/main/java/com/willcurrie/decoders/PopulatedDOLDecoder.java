package com.willcurrie.decoders;

import com.willcurrie.DecodedData;
import com.willcurrie.Decoder;
import com.willcurrie.tlv.BerTlv;
import com.willcurrie.tlv.ISOUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class PopulatedDOLDecoder implements Decoder {
    @Override
    public List<DecodedData> decode(String input, int startIndexInBytes) {
        String[] fields = input.split(":");
        String pdol = fields[0];
        String populatedPDOL = fields[1];
        return decode(pdol, populatedPDOL, pdol.length()/2);
    }

    public List<DecodedData> decode(String pdol, String populatedPDOL, int startIndexInBytes) {
        ArrayList<DecodedData> decoded = new ArrayList<DecodedData>();
        ByteBuffer values = ByteBuffer.wrap(ISOUtil.hex2byte(populatedPDOL));
        List<DOLParser.DOLElement> elements = new DOLParser().parse(ISOUtil.hex2byte(pdol));
        int offset = startIndexInBytes;
        for (DOLParser.DOLElement element : elements) {
            byte[] value = new byte[element.getLength()];
            values.get(value);
            decoded.add(new DecodedData(element.getTag().toString(), ISOUtil.hexString(value), offset, offset + value.length));
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
