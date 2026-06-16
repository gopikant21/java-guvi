package day5;

public class MyThreadDeomon extends Thread{
    private int delay;
    public MyThreadDeomon(String name, int delay){
        super(name);
        this.delay = delay;
    }

    @Override
    public void run(){
        for(int i=0; i<10; i++){
            System.out.println(i + " "+ this.getName() + "\n");
        }
    }
}
