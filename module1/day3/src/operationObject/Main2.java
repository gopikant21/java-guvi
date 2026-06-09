package operationObject;

public class Main2 {
    public static void main(String[] args) {
        Person o1 = new Person("Sachin", 56);
        Person o2 = new Person("Sachin", 56);
        Object o3 = o1;
        System.out.println(o1 == o2);
        System.out.println(o1 == o3);
        System.out.println(o1.hashCode());
        System.out.println(o1.hashCode() == o3.hashCode()); //bucket id
        System.out.println(o1.getClass().getSimpleName());
        System.out.println(o2.getClass().getName());
        System.out.println(o1);
    }
}
