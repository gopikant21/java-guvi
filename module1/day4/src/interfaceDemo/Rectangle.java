package interfaceDemo;

public class Rectangle implements Shape{
    private int length;
    private int breadth;

    @Override
    public void calculateArea(){
        System.out.println("Rectangle's area: " + this.length * this.breadth);
    }

    public Rectangle(int length, int breadth){
        this.length = length;
        this.breadth = breadth;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getBreadth() {
        return breadth;
    }

    public void setBreadth(int breadth) {
        this.breadth = breadth;
    }
}
