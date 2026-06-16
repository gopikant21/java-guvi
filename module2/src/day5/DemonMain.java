package day5;

public class DemonMain {
    public static void main(String[] args) {
        MyThreadDeomon t1 = new MyThreadDeomon("Sachin", 500);
        MyThreadDeomon t2 = new MyThreadDeomon("Saurav", 1000);
        MyThreadDeomon t3 = new MyThreadDeomon("Rahul", 1500);

        t3.setDaemon(true);
        t2.setDaemon(true);

        t1.start();
        t2.start();
        t3.start();

        System.out.println("Exiting Main thread!!");
    }
}
