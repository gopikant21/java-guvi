package wrapperClass;

public class MakeLong {
    public static void main(String[] args) {
        long value = 10;
        Long i = Long.valueOf(value); //Boxing
        System.out.println(i);

        //autoBoxing
        Long j = value;
        System.out.println(j);

        long unboxedValue = i.longValue(); //unboxing
        System.out.println(unboxedValue);
    }
}
