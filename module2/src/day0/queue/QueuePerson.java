package day0.queue;

import day0.list.Person;

import java.util.LinkedList;
import java.util.Queue;

public class QueuePerson {
    public static void main(String[] args) {
        Person p1 = new Person("Alice", 25);
        Person p2 = new Person("Bob", 30);
        Person p3 = new Person("Charlie", 22);
        Person p4 = new Person("Diana", 28);
        Person p5 = new Person("Ethan", 35);

        Queue<Person> q = new LinkedList<Person>();
        q.add(p1);
        q.add(p2);
        q.add(p3);
        q.add(p4);
        q.add(p5);

        System.out.println(q);
    }
}
