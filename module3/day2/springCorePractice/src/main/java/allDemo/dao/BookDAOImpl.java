package allDemo.dao;

import allDemo.entity.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookDAOImpl implements BookDAO {

    private List<Book> books = new ArrayList<>();

    @Override
    public void addBook(Book book) {
        books.add(book);
    }

    @Override
    public List<Book> getAllBooks() {
        return books;
    }

    @Override
    public Book getBookById(int id) {

        for (Book book : books) {
            if (book.getBookId() == id) {
                return book;
            }
        }
        return null;
    }

    @Override
    public List<Book> getBooksByAuthor(String author) {

        List<Book> result = new ArrayList<>();

        for (Book book : books) {
            if (book.getAuthor().equalsIgnoreCase(author)) {
                result.add(book);
            }
        }
        return result;
    }

    @Override
    public List<Book> getBooksByCategory(String category) {

        List<Book> result = new ArrayList<>();

        for (Book book : books) {
            if (book.getCategory().equalsIgnoreCase(category)) {
                result.add(book);
            }
        }
        return result;
    }

    @Override
    public boolean updatePrice(int bookId, double newPrice) {

        Book book = getBookById(bookId);

        if (book != null) {
            book.setPrice(newPrice);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteBook(int bookId) {

        Book book = getBookById(bookId);

        if (book != null) {
            books.remove(book);
            return true;
        }

        return false;
    }

    @Override
    public int getTotalBooks() {
        return books.size();
    }
}
