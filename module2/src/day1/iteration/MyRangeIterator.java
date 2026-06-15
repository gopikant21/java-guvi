package day1.iteration;

import java.util.*;

public class MyRangeIterator implements Iterator {
    private int start;
    private int end;

    public MyRangeIterator(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean hasNext(){
        return start <= end;
    }

    @Override
    public Object next(){
        int old = start;
        start += 2;
        return old;
    }
}
