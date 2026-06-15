package day1.iteration;

import java.util.*;

public class Main1 {
    public static void main(String[] args) {
        //List<String> list = new ArrayList<>();
        //List<String> list = new LinkedList<>();
        //Set<String> list = new HashSet<>();
        Set<String> list = new LinkedHashSet<>();
        list.add("BMW");
        list.add("Ford");
        list.add("Toyota");
        list.add("Audi");

        for(String s : list){
            System.out.println(s);
        }
        System.out.println("-------------------------------");
        Iterator<String> itr = list.iterator();
        System.out.println(itr.getClass().getName());
        while(itr.hasNext()){
            System.out.println(itr.next());
        }
        System.out.println("----------------------------------");
    }
}
