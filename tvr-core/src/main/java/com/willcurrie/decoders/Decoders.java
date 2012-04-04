package com.willcurrie.decoders;

import com.willcurrie.Decoder;

public class Decoders {
    public static final Decoder PRIMITIVE = new NullDecoder();
    public static final Decoder CVM_LIST = new CVMListDecoder();
    public static final Decoder CVM_RESULTS = new CVMResultsDecoder();
    public static final Decoder TVR = new TVRDecoder();
    public static final Decoder DOL = new DataObjectListDecoder();
    public static final Decoder AIP = new AIPDecoder();
    public static final Decoder TSI = new TSIDecoder();
}
