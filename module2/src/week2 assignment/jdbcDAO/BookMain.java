package day4.jdbcDAO;

import java.sql.Connection;
import java.util.List;

import day4.jdbcDAO.dao.BookDAO;
import day4.jdbcDAO.ui.BookDAOimpl;
import day4.jdbcDAO.entity.Book;
import day4.jdbcDAO.connection.DBManager;

public class BookMain {
    public static void main(String[] args) {

        Connection conn = null;

        try {
            conn = DBManager.getConnection();

            BookDAO dao = new BookDAOimpl(conn);

            Book b1 = new Book(0, "Java Basics", "James", "Oracle");
            dao.addBook(b1);

            Book b2 = new Book(0, "Spring Boot", "Pivotal", "VMware");
            dao.addBook(b2);

            Book book = dao.getBookById(1);
            System.out.println("By ID: " + book);

            List<Book> all = dao.getAllBooks();
            System.out.println("All Books:");
            for (Book b : all) {
                System.out.println(b);
            }

            if (book != null) {
                book.setTitle("Advanced Java");
                dao.updateBook(book);
            }

            List<Book> byAuthor = dao.getBooksByAuthor("James");
            System.out.println("Books by Author:");
            for (Book b : byAuthor) {
                System.out.println(b);
            }

            System.out.println("Count: " + dao.countBooks());

            List<Book> sorted = dao.sortByTitle();
            System.out.println("Sorted:");
            for (Book b : sorted) {
                System.out.println(b);
            }

            System.out.println("Exists ID 1: " + dao.exists(1));

            dao.deleteBook(2);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.closeConnection(conn);
        }
    }
}