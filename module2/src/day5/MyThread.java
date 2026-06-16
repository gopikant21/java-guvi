package day5;

import java.io.IOException;
import java.io.Writer;

public class MyThread extends Thread{
    private Writer w;
    public MyThread(String name, Writer w){
        super(name);
        this.w = w;
    }

    @Override
    public void run(){
        for(int i=0; i<100; i++){
            try {
                w.write(i + " "+ this.getName() + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
