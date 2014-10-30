package io.github.binaryfoo.cmdline

import io.github.binaryfoo.DecodedData

import java.io.PrintStream

public class DecodedWriter(private val out: PrintStream) {

    public fun write(decoded: List<DecodedData>, indent: String) {
        for (d in decoded) {
            out.print(indent)
            if (d.rawData.length() > 0) {
                out.print("[")
                out.print(d.rawData)
                out.print("] ")
            }
            out.print(d.getDecodedData())
            out.print("\n")
            write(d.children, indent + "  ")
        }
    }
}
