package day0.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListString {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("dghd");
        list.add("dskjas");
        list.add("rwtr");
        list.add("dcbjc");
        System.out.println(list);
        Collections.sort(list);
        System.out.println(list);

    }
}
