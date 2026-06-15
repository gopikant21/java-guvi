package day2.mapsDemo;

import java.util.*;

//map have unique keys
public class Main1 {
    public static void main(String[] args) {
        //Map<String, String> mp = new HashMap<>();
        Map<String, String> mp = new LinkedHashMap<>();
        //Map<String, String> mp = new TreeMap<>(Collections.reverseOrder());
        mp.put("fname", "Sachin");
        mp.put("lname", "Tendulkar");
        mp.put("team", "MI");
        mp.put("position", "Mentor");

        System.out.println(mp);
        System.out.println("--------------------------------------------");
        System.out.println(mp.get("fname"));
        System.out.println(mp.get("lname"));
        System.out.println(mp.get("team"));
        System.out.println(mp.get("position"));
        System.out.println("--------------------------------------------------");

        for(String k : mp.keySet()){
            System.out.println(k + " : " + mp.get(k));
        }

        System.out.println("------------------------------------------");

        for(String v : mp.values()){
            System.out.println(v);
        }
        System.out.println("-------------------------------------------");

        mp.keySet().stream().forEach((String k)-> System.out.println(k + " : "+ mp.get(k)));

        System.out.println("----------------------------------------");

        mp.values().stream().forEach((String v)-> System.out.println(v));

        System.out.println("-----------------------------------------------");

        for(Map.Entry<String, String> entry: mp.entrySet()){
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }

        System.out.println("----------------------------------------");
        mp.entrySet().stream().forEach((Map.Entry<String, String> entry)-> System.out.println(entry.getKey() + ":" + entry.getValue()));

        System.out.println("-----------------------------------------------------");
    }
}
