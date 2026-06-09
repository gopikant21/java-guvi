package demo;

public class SciCal extends Calculator {
    protected int add(int a, int b) {
        System.out.println("child int addition: ");
        return a + b;
    }
}
