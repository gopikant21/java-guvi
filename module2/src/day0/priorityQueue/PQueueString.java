package day0.priorityQueue;

import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class PQueueString {
    public static void main(String[] args) {
        Queue<String> q = new PriorityQueue<String>(Collections.reverseOrder());
        q.add("hdgsj");
        q.add("sbak");
        q.add("uiewy");
        q.add("bcjkx");
        q.add("kjh");

        System.out.println(q.remove());
        System.out.println(q.remove());


    }
}
