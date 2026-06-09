package Encapsulation;

public class Superhero {
    private String name;
    private String superpower;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setSuperpower(String superpower) {
        this.superpower = superpower;
    }

    public String getSuperpower() {
        return superpower;
    }

    public void useSuperpower(){
        System.out.println(name + " uses " + superpower);
    }

    public static void saveTheWorld(){
        System.out.println("All superhero Save the world");
    }
}


