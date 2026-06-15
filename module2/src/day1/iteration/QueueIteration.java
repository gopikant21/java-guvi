package day1.iteration;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class QueueIteration {
    public static void main(String[] args) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(1);
        queue.add(2);
        queue.add(3);
        queue.add(4);
        queue.add(5);

        for(Integer s : queue){
            System.out.println(s);
        }
        System.out.println("-------------------------------");
        Iterator<Integer> itr = queue.iterator();
        System.out.println(itr.getClass().getName());
        while(itr.hasNext()){
            System.out.println(itr.next());
        }
        System.out.println("----------------------------------");


    }
}
