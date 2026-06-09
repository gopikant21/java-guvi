package operationObject;

public class Main1 {
    public static void main(String[] args) {
        Object o1 = new Object();
        Object o2 = new Object();
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
