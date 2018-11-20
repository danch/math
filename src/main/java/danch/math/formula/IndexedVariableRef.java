package danch.math.formula;

/** a variable ref representing a member of a series indexed by integers */
public class IndexedVariableRef extends VariableRef {
    private int[] indices;

    public IndexedVariableRef(char symbol, int[] indices) {
        super(symbol);
        this.indices = indices;
    }

    public IndexedVariableRef(char symbol, int index) {
        super(symbol);
        this.indices = new int[] { index };
    }

    public int getSeriesIndex() {
        return indices[0];
    }

    public void setSeriesIndex(int seriesIndex) {
        this.indices[0] = seriesIndex;
    }

    @Override
    public boolean equals(Object other) {
        //could do something here, if we introduce a way for Tagged and Indexed variable refs to map
        //  each other from within the library. Client code is very likely to need to do this, so some
        //  map function installed somewhere in here would do it. Scala implicit might be appropriate, but might
        //  also be a pain in the ass - we'd minimally need available a default null map
        if (!(other instanceof IndexedVariableRef))
            return false;

        IndexedVariableRef rhs = (IndexedVariableRef)other;

        if (indices.length != rhs.indices.length)
            return false;

        if (seriesSymbol != rhs.seriesSymbol)
            return false;

        for (int i=0;i<indices.length;i++) {
            if (indices[i]!=rhs.indices[i]) {
                return false;
            }
        }
        return true;
    }

    private String seriesToString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append('[');
        for (int i: indices) {
            if (i!=0)
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
