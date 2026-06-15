package day1.doaArchitecture.dao;

import day1.doaArchitecture.entity.Book;

public interface BookDAO {
    public void save(Book book);
    public Book findById(int id);
    public void deleteById(int id);
    public void update(Book book);
    public void deleteAll();
    public Iterable<Book> findAll();
    public Iterable<Book> findByAuthor(String author);
    public Iterable<Book> findByTitle(String title);
    public Iterable<Book> sortByTitle();
    public Iterable<Book> sortByTitleAsc();
    public Iterable<Book> sortByTitleDesc();
}
