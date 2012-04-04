package com.willcurrie.decoders;

import com.willcurrie.DecodedData;
import com.willcurrie.Decoder;
import com.willcurrie.EmvTags;
import com.willcurrie.TagMetaData;
import com.willcurrie.decoders.apdu.APDUCommand;
import com.willcurrie.tlv.Tag;

import java.util.Arrays;
import java.util.List;

public class ResponseFormat1Decoder implements Decoder {
    @Override
    public List<DecodedData> decode(String input, int startIndexInBytes, DecodeSession decodeSession) {
        if (decodeSession.getCommand() == APDUCommand.GetProcessingOptions) {
            String aip = input.substring(0, 4);
            String afl = input.substring(4);
            return Arrays.asList(
                    decode(EmvTags.APPLICATION_INTERCHANGE_PROFILE, aip, startIndexInBytes, 2, decodeSession),
                    decode(EmvTags.AFL, afl, startIndexInBytes + 2, (input.length() - 4)/2, decodeSession)
            );
        }
        if (decodeSession.getCommand() == APDUCommand.GenerateAC) {
            String cid = input.substring(0, 2);
            String atc = input.substring(2, 6);
            String applicationCryptogram = input.substring(6, 22);
            String issuerApplicationData = input.substring(22);
            return Arrays.asList(
                    decode(EmvTags.CRYPTOGRAM_INFORMATION_DATA, cid, startIndexInBytes, 1, decodeSession),
                    decode(EmvTags.APPLICATION_TRANSACTION_COUNTER, atc, startIndexInBytes + 1, 2, decodeSession),
                    decode(EmvTags.APPLICATION_CRYPTOGRAM, applicationCryptogram, startIndexInBytes + 3, 8, decodeSession),
                    decode(EmvTags.ISSUER_APPLICATION_DATA, issuerApplicationData, startIndexInBytes + 11, (input.length() - 22)/2, decodeSession)
            );
        }
        return null;
    }

    private DecodedData decode(Tag tag, String aip, int startIndexInBytes, int length, DecodeSession decodeSession) {
        TagMetaData tagMetaData = decodeSession.getTagMetaData();
        List<DecodedData> children = tagMetaData.get(tag).getDecoder().decode(aip, startIndexInBytes, decodeSession);
        return new DecodedData(tag, tag.toString(tagMetaData), aip, startIndexInBytes, startIndexInBytes + length, children);
    }

    @Override
    public String validate(String input) {
        return null;
    }

    @Override
    public int getMaxLength() {
        return 0;
    }
}
