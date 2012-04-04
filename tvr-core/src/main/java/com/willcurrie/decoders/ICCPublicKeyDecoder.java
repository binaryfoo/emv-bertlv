package com.willcurrie.decoders;

import com.willcurrie.tlv.ISOUtil;

public class ICCPublicKeyDecoder {

    public String decode(byte[] recovered, int byteLengthOfIssuerModulus) {
        StringBuilder b = new StringBuilder();
        b.append("Header: ").append(ISOUtil.hexString(recovered, 0, 1)).append('\n');
        b.append("Format: ").append(ISOUtil.hexString(recovered, 1, 1)).append('\n');
        b.append("PAN: ").append(ISOUtil.hexString(recovered, 2, 10)).append('\n');
        b.append("Expiry Date (MMYY): ").append(ISOUtil.hexString(recovered, 12, 2)).append('\n');
        b.append("Serial number: ").append(ISOUtil.hexString(recovered, 14, 3)).append('\n');
        b.append("Hash algorithm: ").append(ISOUtil.hexString(recovered, 17, 1)).append('\n');
        b.append("Public key algorithm: ").append(ISOUtil.hexString(recovered, 18, 1)).append('\n');
        String publicKeyLength = ISOUtil.hexString(recovered, 19, 1);
        int i = Integer.parseInt(publicKeyLength, 16);
        b.append("Public key length: ").append(publicKeyLength).append(" (").append(i).append(")").append('\n');
        b.append("Public key exponent length: ").append(ISOUtil.hexString(recovered, 20, 1)).append('\n');
        b.append("Public key: ").append(ISOUtil.hexString(recovered, 21, byteLengthOfIssuerModulus-42)).append('\n');
//        b.append("          : ").append(ISOUtil.hexString(recovered, 20, i)).append('\n');
        b.append("Hash: ").append(ISOUtil.hexString(recovered, 21 + byteLengthOfIssuerModulus-42, 20)).append('\n');
        b.append("Trailer: ").append(ISOUtil.hexString(recovered, 21+byteLengthOfIssuerModulus-42+20, 1)).append('\n');
        return b.toString();
    }
}
