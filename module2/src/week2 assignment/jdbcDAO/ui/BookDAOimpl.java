package day4.jdbcDAO.ui;

import java.sql.*;
import java.util.*;
import day4.jdbcDAO.dao.BookDAO;
import day4.jdbcDAO.entity.Book;

public class BookDAOimpl implements BookDAO {

    private Connection conn;

    public BookDAOimpl(Connection conn) {
        this.conn = conn;
    }

    public void addBook(Book book) {
        try {
            String sql = "INSERT INTO book(title, author, publisher) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getPublisher());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Book getBookById(int id) {
        try {
            String sql = "SELECT * FROM book WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("publisher"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Book> getAllBooks() {
        List<Book> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM book";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                list.add(new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("publisher")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateBook(Book book) {
        try {
            String sql = "UPDATE book SET title=?, author=?, publisher=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getPublisher());
            ps.setInt(4, book.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteBook(int id) {
        try {
            String sql = "DELETE FROM book WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Book> getBooksByAuthor(String author) {
        List<Book> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM book WHERE author=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, author);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("publisher")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Book> getBooksByTitle(String title) {
        List<Book> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM book WHERE title LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, title + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("publisher")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countBooks() {
        try {
            String sql = "SELECT COUNT(*) FROM book";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Book> sortByTitle() {
        List<Book> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM book ORDER BY title";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                list.add(new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("publisher")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean exists(int id) {
        try {
            String sql = "SELECT 1 FROM book WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteAll() {
        try {
            String sql = "DELETE FROM book";
            Statement st = conn.createStatement();
            st.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}