package io.github.binaryfoo.decoders;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.Decoder;
import io.github.binaryfoo.EmvTags;
import io.github.binaryfoo.TagMetaData;
import io.github.binaryfoo.decoders.apdu.APDUCommand;
import io.github.binaryfoo.tlv.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ResponseFormat1Decoder implements Decoder {
    @Override
    public List<DecodedData> decode(@NotNull String input, int startIndexInBytes, @NotNull DecodeSession decodeSession) {
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

    private DecodedData decode(Tag tag, String value, int startIndexInBytes, int length, DecodeSession decodeSession) {
        TagMetaData tagMetaData = decodeSession.getTagMetaData();
        List<DecodedData> children = tagMetaData.get(tag).getDecoder().decode(value, startIndexInBytes, decodeSession);
        return new DecodedData(tag, tag.toString(tagMetaData), tagMetaData.get(tag).decodePrimitiveTlvValue(value), startIndexInBytes, startIndexInBytes + length, children);
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
