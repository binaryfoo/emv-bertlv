package io.github.binaryfoo.decoders;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.Decoder;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * From Visa Contactless Payment Specification v2.1 pD-26
 *      Visa Integrated Circuit Card Card Specification, Version 1.4.0 pA-12
 */
public class VisaCardVerificationResultsDecoder implements Decoder {

    @Override
    public List<DecodedData> decode(String input, int startIndexInBytes, DecodeSession decodeSession) {
        List<DecodedData> decoded = new ArrayList<DecodedData>();
        int cvr = Integer.parseInt(input, 16);
        addIfSet(0x00c00000, 0x0, "AAC returned in second GENERATE AC", cvr, decoded, startIndexInBytes, 1);
        addIfSet(0x00c00000, 0x00400000, "TC returned in second GENERATE AC", cvr, decoded, startIndexInBytes, 1);
        addIfSet(0x00c00000, 0x00800000, "Second GENERATE AC not requested", cvr, decoded, startIndexInBytes, 1);
        addIfSet(0x00300000, 0x00000000, "AAC returned in GPO/first GENERATE AC", cvr, decoded, startIndexInBytes, 1);
        addIfSet(0x00300000, 0x00100000, "TC returned in GPO/first GENERATE AC", cvr, decoded, startIndexInBytes, 1);
        addIfSet(0x00300000, 0x00200000, "ARQC returned in GPO/first GENERATE AC", cvr, decoded, startIndexInBytes, 1);
        addIfSet(0x00300000, 0x00300000, "AAR returned in first GENERATE AC", cvr, decoded, startIndexInBytes, 1);
        addIfSet(0x00080000, "Issuer authentication performed and failed", cvr, decoded, startIndexInBytes, 1);
        addIfSet(0x00040000, "Offline PIN performed", cvr, decoded, startIndexInBytes, 1);
        addIfSet(0x00020000, "Offline PIN verification failed", cvr, decoded, startIndexInBytes, 1);
        addIfSet(0x00010000, "Unable to go online", cvr, decoded, startIndexInBytes, 1);
        addIfSet(0x00008000, "Last online transaction not completed", cvr, decoded, startIndexInBytes, 2);
        addIfSet(0x00004000, "PIN Try Limit exceeded", cvr, decoded, startIndexInBytes, 2);
        addIfSet(0x00002000, "Exceeded velocity checking counters", cvr, decoded, startIndexInBytes, 2);
        addIfSet(0x00001000, "New card", cvr, decoded, startIndexInBytes, 2);
        addIfSet(0x00000800, "Issuer Authentication failure on last online transaction", cvr, decoded, startIndexInBytes, 2);
        addIfSet(0x00000400, "Issuer Authentication not performed after online authorization", cvr, decoded, startIndexInBytes, 2);
        addIfSet(0x00000200, "Application blocked by card because PIN Try Limit exceeded", cvr, decoded, startIndexInBytes, 2);
        addIfSet(0x00000100, "Offline static data authentication failed on last transaction and transaction declined offline", cvr, decoded, startIndexInBytes, 2);
        int scriptsProcessedOnLastTransaction = cvr & 0x000000f0;
        if (scriptsProcessedOnLastTransaction > 0) {
            decoded.add(value(scriptsProcessedOnLastTransaction + " Issuer Script Commands processed on last transaction", startIndexInBytes,3));
        }
        addIfSet(0x00000008, "Issuer Script processing failed on last transaction", cvr, decoded, startIndexInBytes, 3);
        addIfSet(0x00000004, "Offline dynamic data authentication failed on last transaction and transaction declined offline", cvr, decoded, startIndexInBytes, 3);
        addIfSet(0x00000002, "Offline dynamic data authentication performed", cvr, decoded, startIndexInBytes, 3);
        return decoded;
    }

    private void addIfSet(int field, int value, String decodedData, int cvr, List<DecodedData> decoded, int startIndexInBytes, int offset) {
        if (bitSet(cvr, field, value)) {
            decoded.add(value(decodedData, startIndexInBytes, offset));
        }
    }

    private void addIfSet(int value, String description, int cvr, List<DecodedData> decoded, int startIndexInBytes, int offset) {
        if (bitSet(cvr, value)) {
            String byteLabel = ByteLabeller.labelFor(StringUtils.leftPad(Integer.toHexString(value), 8, '0'));
            decoded.add(value("(" + byteLabel + ") " + description, startIndexInBytes, offset));
        }
    }

    private DecodedData value(String decodedData, int startIndexInBytes, int offset) {
        return new DecodedData("", decodedData, startIndexInBytes + offset, startIndexInBytes + offset + 1);
    }

    private boolean bitSet(int cvr, int field, int value) {
        return (cvr & field) == value;
    }

    private boolean bitSet(int cvr, int value) {
        return (cvr & value) == value;
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
