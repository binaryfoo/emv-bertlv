package com.willcurrie;

import sun.misc.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            printHelp();
            System.exit(1);
        }
        String tag = args[0];
        String value = args[1];
        String meta = args.length > 2 ? args[2] : "EMV";
        TagInfo tagInfo = RootDecoder.getTagInfo(tag);
        if (value.equals("-")) {
            value = readStandardInput();
        }
        RootDecoder rootDecoder = new RootDecoder();
        List<DecodedData> decoded = rootDecoder.decode(value, meta, tagInfo);
        new DecodedWriter(System.out).write(decoded, "");
    }

    private static void printHelp() {
        System.out.println("Usage Main <decode-type> <value> [<tag-set>]");
        System.out.println("  <decode-type> is one of");
        for (Map.Entry<String, TagInfo> tag : RootDecoder.getSupportedTags()) {
            System.out.println("    " + tag.getKey() + ": " + tag.getValue().getLongName());
        }
        System.out.println("  <value> is the hex string or '-' for standard input");
        System.out.println("  <tag-set> is one of " + RootDecoder.getAllTagMeta() + " defaults to EMV");
    }

    private static String readStandardInput() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder b = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            b.append(line).append(" ");
        }
        return b.toString();
    }
}
