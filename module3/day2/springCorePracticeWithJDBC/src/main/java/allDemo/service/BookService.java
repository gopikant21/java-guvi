package allDemo.service;

import allDemo.dao.BookDAO;
import allDemo.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private BookDAO bookDAO;

    @Autowired
    public BookService(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    public void saveBook(Book book) {
        bookDAO.addBook(book);
    }

    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }

    public Book findBookById(int id) {
        return bookDAO.getBookById(id);
    }

    public List<Book> findBooksByAuthor(String author) {
        return bookDAO.getBooksByAuthor(author);
    }

    public List<Book> findBooksByCategory(String category) {
        return bookDAO.getBooksByCategory(category);
    }

    public boolean updateBookPrice(int id, double price) {
        return bookDAO.updatePrice(id, price);
    }

    public boolean removeBook(int id) {
        return bookDAO.deleteBook(id);
    }

    public int totalBooks() {
        return bookDAO.getTotalBooks();
    }
}
