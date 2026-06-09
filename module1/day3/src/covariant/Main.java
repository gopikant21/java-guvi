package covariant;

public class Main {
    public static void main(String[] args) {
        Student s = new Student("dsvhjsd","vshdgj");
        Student pl = s.getDemo();
        System.out.println(pl.name);
    }
}
