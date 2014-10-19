package io.github.binaryfoo.decoders.apdu;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.EmvTags;
import io.github.binaryfoo.decoders.DecodeSession;
import io.github.binaryfoo.decoders.PopulatedDOLDecoder;
import io.github.binaryfoo.tlv.BerTlv;
import io.github.binaryfoo.tlv.ISOUtil;

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
        String decodedData = populatedPdolTlv.getValue().length == 0 ? "No PDOL included" : "PDOL " + valueAsHexString;
        return DecodedData.constructed("C-APDU: GPO", decodedData, startIndexInBytes, startIndexInBytes + 5 + length + 1, decodedPDOLElements);
    }

    private List<DecodedData> decodePDOLElements(DecodeSession session, String populatedPdol, int startIndexInBytes) {
        String pdol = session.get(EmvTags.PDOL);
        if (pdol != null) {
            return new PopulatedDOLDecoder().decode(pdol, populatedPdol, startIndexInBytes, session);
        } else {
            return Collections.emptyList();
        }
    }
}
