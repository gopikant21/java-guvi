package arrayDemo;

public class WrapperDemo {

    public static void main(String[] args) {

        // Primitive array
        int[] loanAmountsPrimitive = {100000, 200000, 300000};

        // Wrapper class array
        Integer[] loanAmountsWrapper = {100000, 200000, 300000};

        System.out.println("Primitive Array:");
        for (int amt : loanAmountsPrimitive) {
            System.out.println(amt);
        }

        System.out.println("\nWrapper Array:");
        for (Integer amt : loanAmountsWrapper) {
            System.out.println(amt);
        }
    }
}