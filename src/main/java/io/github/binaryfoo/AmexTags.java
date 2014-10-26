package io.github.binaryfoo;

public class AmexTags extends EmvTags {
    public static final TagMetaData METADATA = EmvTags.METADATA.join(TagMetaData.load("amex.yaml"));
}
