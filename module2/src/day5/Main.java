package day5;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        try(Writer w = new FileWriter("demo.txt")){
            Thread t1 = new MyThread("Sachin", w);
            Thread t2 = new MyThread("Rahul", w);

            t1.start();
            t2.start();

            //Thread joining
            /*t1.join();*/
            t2.join();

            w.write("Active Threads: " + Thread.activeCount() + "\n");

            for(int i=0; i<100; i++){
                w.write(i + " " + Thread.currentThread().getName() + "\n");
            }
        }catch(IOException e){
            throw new RuntimeException(e);
        }



    }
}
