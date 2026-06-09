package wrapperClass;

public class MakeShort {
    public static void main(String[] args) {
        short value = 10;
        Short i = Short.valueOf(value); //Boxing
        System.out.println(i);

        //autoBoxing
        Short j = value;
        System.out.println(j);

        short unboxedValue = i.shortValue(); //unboxing
        System.out.println(unboxedValue);
    }
}
