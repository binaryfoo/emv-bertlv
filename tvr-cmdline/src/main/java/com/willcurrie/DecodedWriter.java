package com.willcurrie;

import java.io.PrintStream;
import java.util.List;

public class DecodedWriter {
    private PrintStream out;

    public DecodedWriter(PrintStream out) {
        this.out = out;
    }

    public void write(List<DecodedData> decoded, String indent) {
        for (DecodedData d : decoded) {
            out.print(indent);
            if (d.getRawData() != null && d.getRawData().length() > 0) {
                out.print("[");
                out.print(d.getRawData());
                out.print("] ");
            }
            out.print(d.getDecodedData());
            out.print("\n");
            write(d.getChildren(), indent + "  ");
        }
    }
}
