package operationString;

//whatever stored in constant pool, are immutable.
public class Main1 {
    public static void main(String[] args) {
        String s1 = "Hello";
        String s2 = "Hello";
        String s3 = new String("Hello");

        System.out.println(s1 == s2);
        System.out.println(s1 == s3);
        System.out.println(s1 == s3.intern());
        System.out.println(s1.equals(s3));
        s2 = s2 + "World";
        System.out.println(s1 == s2);
        System.out.println("s1= " + s1);
        System.out.println("s2= " + s2);
    }
}
