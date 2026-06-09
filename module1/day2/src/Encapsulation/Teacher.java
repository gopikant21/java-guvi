package Encapsulation;

public class Teacher {
    private String name;
    private String subject;
    private int experience;

    public Teacher(String name, String subject, int experience) {
        this.name = name;
        this.subject = subject;
        this.experience = experience;
    }

    public void getDetails() {
        System.out.println("Name : " + name);
        System.out.println("Subject : " + subject);
        System.out.println("Experience : " + experience);
    }

    public void teach() {
        System.out.println("teaching");
    }
}
