package day1.iteration;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class PersonIteration {
    public static void main(String[] args) {
        Set<Person> list = new LinkedHashSet<>();
        Person p1 = new Person("Alice", 25);
        Person p2 = new Person("Bob", 30);
        Person p3 = new Person("Charlie", 22);
        Person p4 = new Person("Diana", 28);
        Person p5 = new Person("Ethan", 35);
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);
        list.add(p5);

        for(Person s : list){
            System.out.println(s);
        }
        System.out.println("-------------------------------");
        Iterator<Person> itr = list.iterator();
        System.out.println(itr.getClass().getName());
        while(itr.hasNext()){
            System.out.println(itr.next());
        }
        System.out.println("----------------------------------");
    }
}
