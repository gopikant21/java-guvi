package intro;

public class Main {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder("Hello");
        System.out.println(sb);

        sb.append("world");
        System.out.println(sb);

        sb.insert(0, "Hi");
        System.out.println(sb);

        sb.delete(0, 3);
        System.out.println(sb);

        sb.replace(6, 11, "Java");
        System.out.println(sb);

        sb.reverse();
        System.out.println(sb);
    }
}
