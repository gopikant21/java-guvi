package operationString;

public class Main {
    public static void main(String[] args) {
        String a = "Hello";
        String b = "World";

        System.out.println(a + " " + b);        // Hello World
        System.out.println(a.concat(b));        // HelloWorld

        String str = "Programming";

        System.out.println(str.substring(3));    // gramming
        System.out.println(str.substring(0, 6)); // Progra
    }
}
