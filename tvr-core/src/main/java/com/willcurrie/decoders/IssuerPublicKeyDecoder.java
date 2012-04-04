package com.willcurrie.decoders;

import com.willcurrie.tlv.ISOUtil;

public class IssuerPublicKeyDecoder {

    public String decode(byte[] recovered, int byteLengthOfCAModulus) {
        StringBuilder b = new StringBuilder();
        b.append("Header: ").append(ISOUtil.hexString(recovered, 0, 1)).append('\n');
        b.append("Format: ").append(ISOUtil.hexString(recovered, 1, 1)).append('\n');
        b.append("Identifier (PAN prefix): ").append(ISOUtil.hexString(recovered, 2, 4)).append('\n');
        b.append("Expiry Date (MMYY): ").append(ISOUtil.hexString(recovered, 6, 2)).append('\n');
        b.append("Serial number: ").append(ISOUtil.hexString(recovered, 8, 3)).append('\n');
        b.append("Hash algorithm: ").append(ISOUtil.hexString(recovered, 11, 1)).append('\n');
        b.append("Public key algorithm: ").append(ISOUtil.hexString(recovered, 12, 1)).append('\n');
        String publicKeyLength = ISOUtil.hexString(recovered, 13, 1);
        int i = Integer.parseInt(publicKeyLength, 16);
        b.append("Public key length: ").append(publicKeyLength).append(" (").append(i).append(")").append('\n');
        b.append("Public key exponent length: ").append(ISOUtil.hexString(recovered, 14, 1)).append('\n');
        b.append("Public key: ").append(ISOUtil.hexString(recovered, 15, byteLengthOfCAModulus-36)).append('\n');
        b.append("          : ").append(ISOUtil.hexString(recovered, 15, i)).append('\n');
        b.append("Hash: ").append(ISOUtil.hexString(recovered, 15 + byteLengthOfCAModulus-36, 20)).append('\n');
        b.append("Trailer: ").append(ISOUtil.hexString(recovered, 15+byteLengthOfCAModulus-36+20, 1)).append('\n');
        return b.toString();
    }
}
