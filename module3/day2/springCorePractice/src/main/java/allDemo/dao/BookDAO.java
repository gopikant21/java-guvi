package allDemo.dao;

import allDemo.entity.Book;
import java.util.List;

public interface BookDAO {

    void addBook(Book book);

    List<Book> getAllBooks();

    Book getBookById(int id);

    List<Book> getBooksByAuthor(String author);

    List<Book> getBooksByCategory(String category);

    boolean updatePrice(int bookId, double newPrice);

    boolean deleteBook(int bookId);

    int getTotalBooks();
}
