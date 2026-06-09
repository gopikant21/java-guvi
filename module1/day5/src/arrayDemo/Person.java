package arrayDemo;

public class Person implements Comparable<Person>{
    String fname;
    String lname;
    int age;

    Person(String fname, String lname, int age) {
        this.fname = fname;
        this.lname = lname;
        this.age = age;
    }
    public String getFname() {
        return fname;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }
    public String getLname() {
        return lname;
    }
    public void setLname(String lname) {
        this.lname = lname;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }


    // Sorting logic (by age)
    @Override
    public int compareTo(Person other) {
        return this.age - other.age;
    }
/*
    @Override
    public int compareTo(Person other) {
        return this.fname.compareTo(other.fname);
    }*/

    @Override
    public String toString() {
        return fname + " " + lname + " - Age: " + age;
    }

}
