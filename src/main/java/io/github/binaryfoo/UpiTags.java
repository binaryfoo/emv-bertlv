package io.github.binaryfoo;

public class UpiTags extends EmvTags {
    public static final TagMetaData METADATA = EmvTags.METADATA.join(TagMetaData.load("upi.yaml"));
}
