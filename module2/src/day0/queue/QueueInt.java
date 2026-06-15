package day0.queue;

import java.util.LinkedList;
import java.util.Queue;

public class QueueInt {
    public static void main(String[] args) {
        Queue<Integer> q = new LinkedList<Integer>();
        q.add(1);
        q.add(2);
        q.add(3);
        q.add(4);
        q.add(5);

        System.out.println(q.size());
        System.out.println(q.remove());
        System.out.println(q);
    }
}
