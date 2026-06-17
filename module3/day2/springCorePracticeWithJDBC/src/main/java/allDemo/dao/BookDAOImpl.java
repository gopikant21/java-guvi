package allDemo.dao;

import allDemo.entity.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookDAOImpl implements BookDAO {

    private final JdbcTemplate jdbcTemplate;

    public BookDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addBook(Book book) {
        String sql = "INSERT INTO books VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getPrice(),
                book.getQuantity()
        );
    }

    @Override
    public List<Book> getAllBooks() {
        String sql = "SELECT * FROM books";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                )
        );
    }

    @Override
    public Book getBookById(int id) {
        String sql = "SELECT * FROM books WHERE book_id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                        new Book(
                                rs.getInt("book_id"),
                                rs.getString("title"),
                                rs.getString("author"),
                                rs.getString("category"),
                                rs.getDouble("price"),
                                rs.getInt("quantity")
                        ),
                id
        );
    }

    @Override
    public List<Book> getBooksByAuthor(String author) {
        String sql = "SELECT * FROM books WHERE LOWER(author) = LOWER(?)";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        new Book(
                                rs.getInt("book_id"),
                                rs.getString("title"),
                                rs.getString("author"),
                                rs.getString("category"),
                                rs.getDouble("price"),
                                rs.getInt("quantity")
                        ),
                author
        );
    }

    @Override
    public List<Book> getBooksByCategory(String category) {
        String sql = "SELECT * FROM books WHERE LOWER(category) = LOWER(?)";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        new Book(
                                rs.getInt("book_id"),
                                rs.getString("title"),
                                rs.getString("author"),
                                rs.getString("category"),
                                rs.getDouble("price"),
                                rs.getInt("quantity")
                        ),
                category
        );
    }

    @Override
    public boolean updatePrice(int bookId, double newPrice) {
        String sql = "UPDATE books SET price = ? WHERE book_id = ?";
        return jdbcTemplate.update(sql, newPrice, bookId) > 0;
    }

    @Override
    public boolean deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE book_id = ?";
        return jdbcTemplate.update(sql, bookId) > 0;
    }

    @Override
    public int getTotalBooks() {
        String sql = "SELECT COUNT(*) FROM books";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}