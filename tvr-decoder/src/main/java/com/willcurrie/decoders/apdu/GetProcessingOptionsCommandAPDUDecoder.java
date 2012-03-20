package com.willcurrie.decoders.apdu;

import com.willcurrie.DecodedData;
import com.willcurrie.EmvTags;
import com.willcurrie.decoders.PopulatedDOLDecoder;
import com.willcurrie.tlv.BerTlv;
import com.willcurrie.tlv.ISOUtil;

import java.util.Collections;
import java.util.List;

public class GetProcessingOptionsCommandAPDUDecoder implements CommandAPDUDecoder {
    @Override
    public APDUCommand getCommand() {
        return APDUCommand.GetProcessingOptions;
    }

    @Override
    public DecodedData decode(String input, int startIndexInBytes, DecodeSession session) {
        int length = Integer.parseInt(input.substring(8, 10), 16);
        String populatedPdol = input.substring(10, 10 + length * 2);
        BerTlv populatedPdolTlv = BerTlv.parse(ISOUtil.hex2byte(populatedPdol));
        String valueAsHexString = populatedPdolTlv.getValueAsHexString();
        List<DecodedData> decodedPDOLElements = decodePDOLElements(session, valueAsHexString, startIndexInBytes + 5 + populatedPdolTlv.getTag().getBytes().length + populatedPdolTlv.getLengthInBytesOfEncodedLength());
        return new DecodedData("C-APDU: GPO", "PDOL " + valueAsHexString, startIndexInBytes, startIndexInBytes + 5 + length + 1, decodedPDOLElements);
    }

    private List<DecodedData> decodePDOLElements(DecodeSession session, String populatedPdol, int startIndexInBytes) {
        String pdol = session.get(EmvTags.PDOL);
        if (pdol != null) {
            return new PopulatedDOLDecoder().decode(pdol, populatedPdol, startIndexInBytes);
        } else {
            return Collections.emptyList();
        }
    }
}
