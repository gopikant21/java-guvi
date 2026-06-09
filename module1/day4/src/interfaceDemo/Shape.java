package interfaceDemo;

public interface Shape {
    String color = "red";
    void calculateArea();

    public static void showColor() {
        System.out.println("color: " + color);
    }
}
