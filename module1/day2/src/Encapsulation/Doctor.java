package Encapsulation;

public class Doctor {
    private String name;
    private String specialty;
    private int experience;

    public Doctor(String name, String specialty, int experience){
        this.name = name;
        this.specialty = specialty;
        this.experience = experience;
    }

    public void treat(){
        System.out.println("Treating Doctor");
    }

    public void getDetails(){
        System.out.println("Name: " + name);
        System.out.println("Specialty: " + specialty);
        System.out.println("Experience: " + experience);
    }
}
