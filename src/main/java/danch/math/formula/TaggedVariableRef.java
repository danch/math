package danch.math.formula;


public class TaggedVariableRef extends VariableRef {
    private String[] orderedTags;

    public TaggedVariableRef(char symbol, String[] orderedTags) {
        super(symbol);
        this.orderedTags = orderedTags;
    }

    public TaggedVariableRef(char symbol, String tag) {
        super(symbol);
        this.orderedTags = new String[] { tag };
    }

    public String[] getTags() {
        return orderedTags.clone();
    }

    /** do I really want this? */
    public String getTag() {
        return orderedTags[0];
    }

    public void setTag(String seriesIndex) {
        this.orderedTags[0] = seriesIndex;
    }

    @Override
    public boolean equals(Object other) {
        //could do something here, if we introduce a way for Tagged and Indexed variable refs to map
        //  each other from within the library. Client code is very likely to need to do this, so some
        //  map function installed somewhere in here would do it. Scala implicit might be appropriate, but might
        //  also be a pain in the ass - we'd minimally need available a default null map
        if (!(other instanceof TaggedVariableRef))
            return false;

        TaggedVariableRef rhs = (TaggedVariableRef)other;

        if (orderedTags.length != rhs.orderedTags.length)
            return false;

        if (seriesSymbol != rhs.seriesSymbol)
            return false;

        for (int i = 0; i< orderedTags.length; i++) {
            if (orderedTags[i] == null && rhs.orderedTags[i] == null) {
                continue;
            }
            if (orderedTags[i] == null && rhs.orderedTags[i] != null) {
                return false;
            }
            if (orderedTags[i] != null && rhs.orderedTags[i] == null) {
                return false;
            }
            if (!orderedTags[i].equals(rhs.orderedTags[i])) {
                return false;
            }
        }
        return true;
    }

    private String seriesToString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append('[');
        for (String i: orderedTags) {
            if (buffer.length()>1)
                buffer.append(',');
            buffer.append(i);
        }
        buffer.append(']');
        return buffer.toString();
    }

    @Override
    public String toString() {
        return boundValue.map(val->Literal.format.format(val)).orElse(""+seriesSymbol+seriesToString());
    }
}
