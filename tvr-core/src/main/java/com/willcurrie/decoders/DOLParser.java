package com.willcurrie.decoders;

import com.willcurrie.tlv.Tag;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DOLParser {

    public List<DOLElement> parse(byte[] dol) {
        ArrayList<DOLElement> elements = new ArrayList<DOLElement>();
        ByteBuffer buffer = ByteBuffer.wrap(dol);
        while (buffer.hasRemaining()) {
            Tag tag = Tag.parse(buffer);
            byte length = buffer.get();
            elements.add(new DOLElement(tag, length));
        }
        return elements;
    }

    public static class DOLElement {
        private final Tag tag;
        private final int length;

        public DOLElement(Tag tag, int length) {
            this.length = length;
            this.tag = tag;
        }

        public Tag getTag() {
            return tag;
        }

        public int getLength() {
            return length;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DOLElement that = (DOLElement) o;

            if (length != that.length) return false;
            if (tag != null ? !tag.equals(that.tag) : that.tag != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = tag != null ? tag.hashCode() : 0;
            result = 31 * result + length;
            return result;
        }

        @Override
        public String toString() {
            return "DOLElement{" +
                    "tag=" + tag +
                    ", length=" + length +
                    '}';
        }
    }
}
