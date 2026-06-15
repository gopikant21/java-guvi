package day1.interConversion;

import java.util.*;

public class Demo1 {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(12);
        list.add(27);
        list.add(336);
        list.add(4346);
        list.add(59);
        list.add(64);
        list.add(17);
        list.add(59);
        list.add(64);
        list.add(17);
        list.add(28);
        list.add(906);
        list.add(27);
        list.add(336);
        list.add(4346);

        System.out.println(list);
        System.out.println("---------------------------");
        Set<Integer> set = new LinkedHashSet<>(list);
        System.out.println(set);
        System.out.println("--------------------------");
        List<Integer> list1 = new ArrayList<>(set);
        System.out.println(list1);
        System.out.println("--------------------------");
        System.out.println(list instanceof Set);
        System.out.println(list instanceof List);



    }
}
