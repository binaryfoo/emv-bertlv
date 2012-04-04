package com.willcurrie.decoders;

import com.willcurrie.DecodedData;
import com.willcurrie.Decoder;

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
        if (bitSet(cvr, 0x00c00000, 0x0)) {
            decoded.add(value("AAC returned in second GENERATE AC", startIndexInBytes, 1));
        }
        if (bitSet(cvr, 0x00c00000, 0x00400000)) {
            decoded.add(value("TC returned in second GENERATE AC", startIndexInBytes, 1));
        }
        if (bitSet(cvr, 0x00c00000, 0x00800000)) {
            decoded.add(value("Second GENERATE AC not requested", startIndexInBytes, 1));
        }
        if (bitSet(cvr, 0x00300000, 0x00000000)) {
            decoded.add(value("AAC returned in GPO/first GENERATE AC", startIndexInBytes, 1));
        }
        if (bitSet(cvr, 0x00300000, 0x00100000)) {
            decoded.add(value("TC returned in GPO/first GENERATE AC", startIndexInBytes, 1));
        }
        if (bitSet(cvr, 0x00300000, 0x00200000)) {
            decoded.add(value("ARQC returned in GPO/first GENERATE AC", startIndexInBytes, 1));
        }
        if (bitSet(cvr, 0x00300000, 0x00300000)) {
            decoded.add(value("AAR returned in first GENERATE AC", startIndexInBytes, 1));
        }
        if (bitSet(cvr, 0x00080000)) {
            decoded.add(value("Issuer authentication performed and failed", startIndexInBytes,1));
        }
        if (bitSet(cvr, 0x00040000)) {
            decoded.add(value("Offline PIN performed", startIndexInBytes,1));
        }
        if (bitSet(cvr, 0x00020000)) {
            decoded.add(value("Offline PIN verification failed", startIndexInBytes,1));
        }
        if (bitSet(cvr, 0x00010000)) {
            decoded.add(value("Unable to go online", startIndexInBytes,1));
        }
        if (bitSet(cvr, 0x00008000)) {
            decoded.add(value("Last online transaction not completed", startIndexInBytes,2));
        }
        if (bitSet(cvr, 0x00004000)) {
            decoded.add(value("PIN Try Limit exceeded", startIndexInBytes,2));
        }
        if (bitSet(cvr, 0x00002000)) {
            decoded.add(value("Exceeded velocity checking counters", startIndexInBytes,2));
        }
        if (bitSet(cvr, 0x00001000)) {
            decoded.add(value("New card", startIndexInBytes,2));
        }
        if (bitSet(cvr, 0x00000800)) {
            decoded.add(value("Issuer Authentication failure on last online transaction", startIndexInBytes,2));
        }
        if (bitSet(cvr, 0x00000400)) {
            decoded.add(value("Issuer Authentication not performed after online authorization", startIndexInBytes,2));
        }
        if (bitSet(cvr, 0x00000200)) {
            decoded.add(value("Application blocked by card because PIN Try Limit exceeded", startIndexInBytes,2));
        }
        if (bitSet(cvr, 0x00000100)) {
            decoded.add(value("Offline static data authentication failed on last transaction and transaction declined offline", startIndexInBytes,2));
        }
        int scriptsProcessedOnLastTransaction = cvr & 0x000000f0;
        if (scriptsProcessedOnLastTransaction > 0) {
            decoded.add(value(scriptsProcessedOnLastTransaction +" Issuer Script Commands processed on last transaction", startIndexInBytes,3));
        }
        if (bitSet(cvr, 0x00000008)) {
            decoded.add(value("Issuer Script processing failed on last transaction", startIndexInBytes,3));
        }
        if (bitSet(cvr, 0x00000004)) {
            decoded.add(value("Offline dynamic data authentication failed on last transaction and transaction declined offline", startIndexInBytes,3));
        }
        if (bitSet(cvr, 0x00000002)) {
            decoded.add(value("Offline dynamic data authentication performed", startIndexInBytes,3));
        }
        return decoded;
    }

    private DecodedData value(String decodedData, int startIndexInBytes, int offset) {
        return new DecodedData("", decodedData, startIndexInBytes + offset, startIndexInBytes + offset + 1);
    }

    private boolean bitSet(int cvr, int field, int value) {
        return (cvr & field) == value;
    }

    private boolean bitSet(int cvr, int value) {
        return (cvr& value) == value;
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
