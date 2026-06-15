package day0.priorityQueue;

import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class PQueueInt {
    public static void main(String[] args) {
        Queue<Integer> q = new PriorityQueue<Integer>(Collections.reverseOrder());
        q.add(1);
        q.add(25);
        q.add(30);
        q.add(42);
        q.add(5);

        System.out.println(q.size());
        System.out.println(q.remove());
        System.out.println(q.remove());
    }
}
