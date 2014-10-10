package io.github.binaryfoo.decoders;

public class AppUsageControlDecoder extends EmvBitStringDecoder {
    public AppUsageControlDecoder() {
        super("fields/app-usage-control.txt", true);
    }
}
