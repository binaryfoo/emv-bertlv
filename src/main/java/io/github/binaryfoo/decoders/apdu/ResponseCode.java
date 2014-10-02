package io.github.binaryfoo.decoders.apdu;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ResponseCode {

    private static final List<ResponseCode> codes = new ArrayList<>();
    static {
        try (InputStream in = ResponseCode.class.getClassLoader().getResourceAsStream("r-apdu-status.txt")) {
            for (String line : IOUtils.readLines(in)) {
                codes.add(new ResponseCode(
                        line.substring(0, 2),
                        line.substring(3, 5),
                        line.substring(6, 7),
                        line.substring(8)));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load codes", e);
        }
    }

    private final String sw1;
    private final String sw2;
    private final String type;
    private final String description;

    public ResponseCode(String sw1, String sw2, String type, String description) {
        this.sw1 = sw1;
        this.sw2 = sw2;
        this.type = type;
        this.description = description;
    }

    public static ResponseCode lookup(String hex) {
        String sw1 = hex.substring(0, 2);
        String sw2 = hex.substring(2, 4);
        for (ResponseCode code : codes) {
            if (sw1.equals(code.getSw1())) {
                if ("XX".equals(code.getSw2())) {
                    return code;
                }
                if ("--".equals(code.getSw2())) {
                    continue;
                }
                if (sw2.equals(code.getSw2())) {
                    return code;
                }
            }
        }
        return new ResponseCode(sw1, sw2, "", "Unknown");
    }

    public String getSw1() {
        return sw1;
    }

    public String getSw2() {
        return sw2;
    }

    public String getHex() {
        return sw1 + sw2;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
