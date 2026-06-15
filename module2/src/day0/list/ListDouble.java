package day0.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListDouble {
    public static void main(String[] args) {
        List<Double> list = new ArrayList();
        list.add(55.7);
        list.add(682.728);
        list.add(789.0);
        System.out.println(list);

        Collections.sort(list);

        System.out.println(list);
    }
}
