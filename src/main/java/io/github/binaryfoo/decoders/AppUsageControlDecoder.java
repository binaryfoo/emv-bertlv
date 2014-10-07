package io.github.binaryfoo.decoders;

public class AppUsageControlDecoder extends FixedLengthDecoder {
    public AppUsageControlDecoder() {
        super(4, "8000", "Valid for domestic cash transactions",
                 "4000", "Valid for international cash transactions",
                "2000", "Valid for domestic goods",
                "1000", "Valid for international goods",
                "0800", "Valid for domestic services",
                "0400", "Valid for international services",
                "0200", "Valid at ATMs",
                "0100", "Valid at terminals other than ATMs",
                "0080", "Domestic cashback allowed",
                "0040", "International cashback allowed");
    }
}
