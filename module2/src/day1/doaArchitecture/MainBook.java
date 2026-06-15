package day1.doaArchitecture;

import day1.doaArchitecture.entity.Book;
import day1.doaArchitecture.ui.BookDaoImp;

public class MainBook {
    public static void main(String[] args) {

        BookDaoImp dao = new BookDaoImp();

        //Add Books
        dao.save(new Book(1, "Java Basics", "James"));
        dao.save(new Book(2, "Spring Boot", "Rod"));
        dao.save(new Book(3, "Hibernate", "Gavin"));
        dao.save(new Book(4, "Java Advanced", "James"));


        //Find All
        System.out.println("All Books:");
        for (Book b : dao.findAll()) {
            System.out.println(b.getId() + " - " + b.getTitle() + " - " + b.getAuthor());
        }

        //Find by ID
        System.out.println("\nFind by ID (2):");
        Book book = dao.findById(2);
        if (book != null) {
            System.out.println(book.getTitle());
        }

        //Update
        dao.update(new Book(2, "Spring Boot Updated", "Rod"));
        System.out.println("\nAfter Update:");
        for (Book b : dao.findAll()) {
            System.out.println(b.getId() + " - " + b.getTitle());
        }

        // Find by Author
        System.out.println("\nBooks by author 'James':");
        for (Book b : dao.findByAuthor("James")) {
            System.out.println(b.getTitle());
        }

        //Find by Title
        System.out.println("\nFind by title 'Hibernate':");
        for (Book b : dao.findByTitle("Hibernate")) {
            System.out.println(b.getAuthor());
        }

        //Sort Ascending
        System.out.println("\nSorted by Title (Asc):");
        for (Book b : dao.sortByTitleAsc()) {
            System.out.println(b.getTitle());
        }

        //Sort Descending
        System.out.println("\nSorted by Title (Desc):");
        for (Book b : dao.sortByTitleDesc()) {
            System.out.println(b.getTitle());
        }

        //Delete by ID
        dao.deleteById(3);
        System.out.println("\nAfter deleting ID 3:");
        for (Book b : dao.findAll()) {
            System.out.println(b.getTitle());
        }

        //Delete All
        dao.deleteAll();
        System.out.println("\nAfter deleteAll():");
        for (Book b : dao.findAll()) {
            System.out.println(b.getTitle());
        }
    }
}
