package day0;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(10);
        list.add(200);
        list.add(34);
        list.add(47);
        System.out.println(list);
        System.out.println(list.get(0));
        System.out.println(list.get(1));
        System.out.println(list.get(2));
        list.set(0,8);
        list.remove(3);
        System.out.println(list);
        Collections.sort(list);
        System.out.println(list);
    }
}
