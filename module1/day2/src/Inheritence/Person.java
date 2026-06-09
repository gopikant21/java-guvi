package Inheritence;

public class Person {
    protected String name;
    protected int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public String getName() {
        return this.name;
    };

    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return this.age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public void eat() {
        System.out.println("eating");
    }
    public void sleep() {
        System.out.println("sleeping");
    }

    public void showDetails() {
        System.out.println(this.name);
        System.out.println(this.age);
    }
}
