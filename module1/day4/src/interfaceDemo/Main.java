package interfaceDemo;

public class Main {
    public static void main(String[] args) {
        Shape c = new Circle(10);
        c.calculateArea();

        Shape r = new Rectangle(10,20);
        r.calculateArea();

        System.out.println(Shape.color);
        Shape.showColor();
    }
}
