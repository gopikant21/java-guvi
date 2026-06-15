package day1.iteration;

import java.util.*;

public class MyRange implements Iterable {
    private int start;
    private int end;
    public MyRange(int start, int end)
    {
        this.start = start;
        this.end = end;
    }

    @Override
    public Iterator iterator(){
        return new MyRangeIterator(start, end);
    }

}
