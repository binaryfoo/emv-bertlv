package com.willcurrie.decoders.apdu;

import com.willcurrie.DecodedData;
import com.willcurrie.EmvTags;
import com.willcurrie.decoders.DecodeSession;
import com.willcurrie.decoders.PopulatedDOLDecoder;
import com.willcurrie.tlv.ISOUtil;

import java.util.Collections;
import java.util.List;

public class GenerateACAPDUDecoder implements CommandAPDUDecoder {
    @Override
    public APDUCommand getCommand() {
        return APDUCommand.GenerateAC;
    }

    @Override
    public DecodedData decode(String input, int startIndexInBytes, DecodeSession session) {
        int length = Integer.parseInt(input.substring(8, 10), 16);
        byte cid = ISOUtil.hex2byte(input.substring(4, 6))[0];
        String cryptogramType = parseCryptogramType(cid);
        boolean cda = (cid & 0x10) == 0x10;
        String populatedCDOL = input.substring(10, 10 + length * 2);
        List<DecodedData> decodedPopulatedCDOL = decodeCDOLElements(session, populatedCDOL, startIndexInBytes + 5);
        return new DecodedData("C-APDU: Generate AC (" + cryptogramType + (cda ? "+CDA" : "") + ")", "CDOL " + populatedCDOL, startIndexInBytes, startIndexInBytes + 5 + length + 1, decodedPopulatedCDOL);
    }
    private String parseCryptogramType(byte b) {
        if ((b & 0x40) == 0x40) {
            return "TC";
        }
        if ((b & (byte) 0x80) == (byte) 0x80) {
            return "ARQC";
        }
        return "AAC";
    }

    private List<DecodedData> decodeCDOLElements(DecodeSession session, String populatedCdol, int startIndexInBytes) {
        String cdol = session.get(EmvTags.CDOL_1);
        if (cdol != null) {
            return new PopulatedDOLDecoder().decode(cdol, populatedCdol, startIndexInBytes);
        } else {
            return Collections.emptyList();
        }
    }
}
