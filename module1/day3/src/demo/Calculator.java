package demo;

public class Calculator {
    /*public long add(long a, long b) {
        System.out.println("long addition: ");
        return (long)(a + b);
    }*/

    protected int add(int a, int b) {
        System.out.println("int addition: ");
        return a + b;
    }

    public float add(float a, float b) {
        System.out.println("float addition: ");
        return a + b;
    }

    public int add(int a, int b, int c) {
        return a + b + c;
    }

    public String add(String a, String b){
        System.out.println("string addition: ");
        return a + b;
    }

    public double add(double a, double b) {
        System.out.println("double addition: ");
        return a + b;
    }

    public byte add(byte a, byte b) {
        System.out.println("byte addition: ");
        return (byte)(a + b);
    }

    public short add(short a, short b) {
        System.out.println("short addition: ");
        return (short)(a + b);
    }


}
