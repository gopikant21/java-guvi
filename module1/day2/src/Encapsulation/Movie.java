package Encapsulation;

public class Movie {
    private String title;
    private String director;
    private int releaseYear;

    public Movie(String title, String director, int releaseYear) {
        this.title = title;
        this.director = director;
        this.releaseYear = releaseYear;
    }

    public String getTitle() {
        return this.title;
    }
    /*public void setTitle(String title) {
        this.title = title;
    }*/
    public String getDirector() {
        return this.director;
    }
    /*public void setDirector(String director) {
        this.director = director;
    }*/
    public int getReleaseYear() {
        return this.releaseYear;
    }
    /*public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }*/

    public void play(){
        System.out.println("Playing movie " + this.title);
    }

    public int getRating(){
        return 5;
    }
}
