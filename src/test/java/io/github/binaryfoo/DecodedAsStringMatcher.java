package io.github.binaryfoo;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.List;

public class DecodedAsStringMatcher extends TypeSafeMatcher<List<DecodedData>> {

    private final String raw;

    public DecodedAsStringMatcher(String raw) {
        this.raw = raw;
    }

    public static TypeSafeMatcher<List<DecodedData>> decodedAsString(String raw) {
        return new DecodedAsStringMatcher(raw);
    }

    @Override
    protected boolean matchesSafely(List<DecodedData> decodedData) {
        return raw.equals(toString(decodedData));
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(raw);
    }

    @Override
    protected void describeMismatchSafely(List<DecodedData> item, Description mismatchDescription) {
        mismatchDescription.appendValue(toString(item));
    }

    private String toString(List<DecodedData> decoded) {
        StringBuilder b = new StringBuilder();
        for (DecodedData d : decoded) {
            if (!"".equals(d.getRawData())) {
                b.append(d.getRawData()).append(": ");
            }
            b.append(d.getDecodedData()).append("\n");
        }
        return b.toString();
    }
}
