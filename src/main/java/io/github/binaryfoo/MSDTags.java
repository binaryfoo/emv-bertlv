package io.github.binaryfoo;

public class MSDTags extends EmvTags {
    public static final TagMetaData METADATA = EmvTags.METADATA.join(TagMetaData.load("msd.yaml"));
}
