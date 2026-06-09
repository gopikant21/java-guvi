package Encapsulation;

public class Book {
    String title;
    String author;
    int pages;

    public Book(String title, String author, int pages) {
        this.title = title;
        this.author = author;
        this.pages = pages;
    }

    public void read(){
        System.out.println("Read " + title);
    }

    public void getSummary(){
        System.out.println("Book getSummary");
    }
}
