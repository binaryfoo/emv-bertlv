package io.github.binaryfoo;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class BoundsMatcher extends TypeSafeMatcher<DecodedData> {

    private final int start;
    private final int end;

    public static TypeSafeMatcher<DecodedData> hasBounds(int start, int end) {
        return new BoundsMatcher(start, end);
    }

    public BoundsMatcher(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected boolean matchesSafely(DecodedData decodedData) {
        return decodedData.getStartIndex() == start && decodedData.getEndIndex() == end;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("start").appendValue(start).appendText(" end").appendValue(end);
    }
}
