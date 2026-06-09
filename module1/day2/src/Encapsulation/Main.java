package Encapsulation;

public class Main {
    public static void main(String[] args) {
        Superhero hero = new Superhero();
        /*hero.name = "Ionman";
        hero.superpower = "techie";*/

        hero.setName("Ionman");
        hero.setSuperpower("techie");

        hero.useSuperpower();
        hero.saveTheWorld();


        /*Person p1 = new Person();
        p1.fname = "Yanga";
        p1.lname = "Rajan";
        p1.age = 23;

        Person.eat();
        Person.walk();
        Person.talk();*/

        /*Book b1 = new Book();
        b1.title = "Book 1";
        b1.author = "auther 1";
        b1.pages = 156;

        b1.read();
        b1.getSummary();*/




    }
}
