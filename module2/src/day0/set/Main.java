package day0.set;

import java.util.*;
import java.util.TreeSet;

public class Main {
    public static void main(String[] args) {
        //Set<Integer> set = new HashSet<>(); //rearrangement, unorganized
        //Set<Integer> set = new LinkedHashSet<>(); //norearrangement
        Set<Integer> set = new TreeSet<>(); //sorted asc
        set.add(25);
        set.add(30);
        set.add(22);
        set.add(28);
        set.add(29);
        set.add(30);
        set.add(28);
        set.add(29);
        System.out.println("set: " + set);
    }
}
