package day0.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListPerson {
    public static void main(String[] args) {

        Person p1 = new Person("Alice", 25);
        Person p2 = new Person("Bob", 30);
        Person p3 = new Person("Charlie", 22);
        Person p4 = new Person("Diana", 28);
        Person p5 = new Person("Ethan", 35);

        List<Person> list = new ArrayList<Person>();
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);
        list.add(p5);

        System.out.println(list);

        Collections.sort(list, (a,b)->a.age-b.age );
        System.out.println(list);
    }
}
