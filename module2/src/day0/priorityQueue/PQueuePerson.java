package day0.priorityQueue;

import java.util.PriorityQueue;
import java.util.Queue;

public class PQueuePerson {
    public static void main(String[] args) {
        Person p1 = new Person("Alice", 25);
        Person p2 = new Person("Bob", 30);
        Person p3 = new Person("Charlie", 22);
        Person p4 = new Person("Diana", 28);
        Person p5 = new Person("Ethan", 35);

        Queue<Person> q = new PriorityQueue<Person>((a,b)->a.getAge()-b.getAge());
        q.add(p1);
        q.add(p2);
        q.add(p3);
        q.add(p4);
        q.add(p5);

        System.out.println(q.remove());
        System.out.println(q.remove());

    }
}
