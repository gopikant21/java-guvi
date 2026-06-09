package AnimalInterface;

public class Dog implements Animal{
    @Override
    public void eat(){
        System.out.println("Dog eat");
    }
    @Override
    public void talk(){
        System.out.println("Dog talk");
    }

    @Override
    public void walk(){
        System.out.println("Dog walk");
    }
}
