package day1.iteration;

import java.util.*;

public class MainRange {
    public static void main(String[] args) {
        MyRange range1 = new MyRange(10,20);
        Iterator itr = range1.iterator();
        while (itr.hasNext()){
            System.out.println(itr.next());
        }
        System.out.println("------------------------");
        for(Object o : range1){
            System.out.println((Integer) o);
        }
    }
}
