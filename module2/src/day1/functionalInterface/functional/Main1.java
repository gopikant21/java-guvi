package day1.functionalInterface.functional;

public class Main1 {
    public static void main(String[] args) {
        /*class GM implements Greeting {
            @Override
            public void greet() {
                System.out.println("Good Morning!");
            }
        }*/

        Greeting gm = new Greeting() {
            @Override
            public void greet() {
                System.out.println("Good Morning!");
            }
        };
        gm.greet();
        System.out.println("------------------------------------");


        /*class GN implements Greeting {
            @Override
            public void greet() {
                System.out.println("Good Night!");
            }
        }*/
        Greeting gn = new Greeting() {
            @Override
            public void greet() {
                System.out.println("Good Night!");
            }
        };
        gn.greet();
        System.out.println("-------------------------------------");

        //Lambda Expression
        Greeting ge = ()->{
            System.out.println("Good Eveneing!");
        };
        ge.greet();
    }
}
