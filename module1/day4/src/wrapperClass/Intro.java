package wrapperClass;

public class Intro {
    public static void main(String[] args) {
        int value = 10;
        Integer i = Integer.valueOf(value); //Boxing
        System.out.println(i);

        //autoBoxing
        Integer j = value;
        System.out.println(j);

        int unboxedValue = i.intValue(); //unboxing
        System.out.println(unboxedValue);


        //Boolean
        boolean boolValue = false;
        Boolean boolI = Boolean.valueOf(boolValue); //Boxing
        System.out.println(boolI);

        //autoBoxing
        Boolean b = boolValue;
        System.out.println(b);

        boolean boolUnboxedValue = boolI.booleanValue(); //unboxing
        System.out.println(boolUnboxedValue);


    }
}
