package io.github.binaryfoo

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import kotlin.platform.platformStatic

public class DecodedAsStringMatcher(private val raw: String) : TypeSafeMatcher<List<DecodedData>>() {

    override fun matchesSafely(decodedData: List<DecodedData>): Boolean {
        return raw == toString(decodedData)
    }

    override fun describeTo(description: Description) {
        description.appendValue(raw)
    }

    override fun describeMismatchSafely(item: List<DecodedData>, mismatchDescription: Description) {
        mismatchDescription.appendValue(toString(item))
    }

    private fun toString(decoded: List<DecodedData>): String = decoded.toSimpleString("")

    class object {

        platformStatic public fun decodedAsString(raw: String): TypeSafeMatcher<List<DecodedData>> = DecodedAsStringMatcher(raw)
    }
}
