package poly;

//autoWidening will have higher priority than autoBoxing
public class Calculator {
    public int add(int a, int b) {
        System.out.println("int: ");
        return a + b;
    }

    public Integer add(Integer a, Integer b) {
        System.out.println("Integer: ");
        return a + b;
    }

    public long add(long a, long b) {
        System.out.println("long: ");
        return a + b;
    }

    public Long add(Long a, Long b) {
        System.out.println("Long: ");
        return a + b;
    }
}
