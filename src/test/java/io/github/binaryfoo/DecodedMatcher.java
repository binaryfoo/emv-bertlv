package io.github.binaryfoo;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class DecodedMatcher extends TypeSafeMatcher<DecodedData> {

  private final String raw;
  private final String decoded;

  public DecodedMatcher(String raw, String decoded) {
    this.raw = raw;
    this.decoded = decoded;
  }

  public static TypeSafeMatcher<DecodedData> decodedAs(String raw, String decoded) {
    return new DecodedMatcher(raw, decoded);
  }

  @Override
  protected boolean matchesSafely(DecodedData decodedData) {
    return raw.equals(decodedData.getRawData()) && decoded.equals(decodedData.getDecodedData());
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("raw ").appendValue(raw);
    description.appendText(" decoded ").appendValue(decoded);
  }
}
