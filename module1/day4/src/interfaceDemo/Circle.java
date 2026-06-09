package interfaceDemo;

public class Circle implements Shape {
    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }
    public double getRadius() {
        return radius;
    }
    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public void calculateArea() {
        System.out.println("Circle area: " + Math.PI * radius * radius);
    }
}
