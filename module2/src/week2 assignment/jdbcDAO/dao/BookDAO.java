package day4.jdbcDAO.dao;

import java.util.List;
import day4.jdbcDAO.entity.Book;

public interface BookDAO {

    void addBook(Book book);

    Book getBookById(int id);

    List<Book> getAllBooks();

    void updateBook(Book book);

    void deleteBook(int id);

    List<Book> getBooksByAuthor(String author);

    List<Book> getBooksByTitle(String title);

    int countBooks();

    List<Book> sortByTitle();

    boolean exists(int id);

    void deleteAll();
}
