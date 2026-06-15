package day0.set;

import day0.list.Person;

import java.util.HashSet;
import java.util.Set;

public class PersonSet {
    public static void main(String[] args) {
        Person p1 = new Person("Alice", 25);
        Person p2 = new Person("Bob", 30);
        Person p3 = new Person("Charlie", 22);
        Person p4 = new Person("Diana", 28);
        Person p5 = new Person("Ethan", 35);
        Person p6 = new Person("Bob", 30);
        Person p7 = new Person("Charlie", 22);
        Person p8 = new Person("Diana", 28);

        Set<Person> set = new HashSet<>();
        set.add(p1);
        set.add(p2);
        set.add(p3);
        set.add(p4);
        set.add(p5);
        set.add(p6);
        set.add(p7);
        set.add(p8);

        System.out.println(set);

    }
}
