package day1.functionalInterface.functional;

import java.util.function.*;

public class MainFunction {
    public static void main(String[] args) {
        Function<String, Integer> d = (String s) ->{
            return s.length();
        };
        System.out.println(d.apply("Hello"));

        BiFunction<String, String, Integer> f = (s1, s2) ->{
            return s1.length() + s2.length();
        };
        System.out.println(f.apply("Hello", "World"));

        UnaryOperator<String> u = (String s) -> {
            return s.length() + s;
        };
        System.out.println(u.apply("Hello"));

        BinaryOperator<Integer> b = (x, y) -> x + y;
        System.out.println(b.apply(10, 20));

        Consumer<String> c = (String s) -> {
            System.out.println(s);
        };

    }
}
