package AnimalInterface;

public class Lion implements Animal {
    @Override
    public void eat(){
        System.out.println("Lion eat");
    }
    @Override
    public void talk(){
        System.out.println("Lion talk");
    }

    @Override
    public void walk(){
        System.out.println("Lion walk");
    }
}
