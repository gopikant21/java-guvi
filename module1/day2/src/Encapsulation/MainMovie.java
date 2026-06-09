package Encapsulation;

public class MainMovie {
    public static void main(String[] args) {
        Movie m1 = new Movie("Harry Potter", "dsfjhkds", 1890);
        /*m1.setTitle("Harry Potter");
        m1.setDirector("vhdsk");
        m1.setReleaseYear(1920);*/

        System.out.println(m1.getTitle()+ " " + m1.getDirector() + " " + m1.getReleaseYear());

        m1.play();
        int r = m1.getRating();
        System.out.println("rating = " + r);
    }
}
