package Encapsulation;

public class Student {
    private String firstName;
    private String lastName;
    private int age;
    public Student(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
    public String getDetils(){
        return "Name: " + this.firstName + " " + this.lastName + " " + this.age;
    }
}
