package day1.doaArchitecture.ui;

import day1.doaArchitecture.dao.BookDAO;
import day1.doaArchitecture.entity.Book;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class BookDaoImp implements BookDAO {
        private List<Book> books = new ArrayList<>();

        // Save
        public void save(Book book){
            books.add(book);
        }

        // Find by ID
        public Book findById(int id){
            for (Book book : books) {
                if (book.getId() == id) {
                    return book;
                }
            }
            return null;
        }

        // Delete by ID
        public void deleteById(int id){
            Iterator<Book> bookItr = books.iterator();
            while(bookItr.hasNext()){
                Book book = bookItr.next();
                if(book.getId() == id){
                    bookItr.remove();
                }
            }
        }

        // Update
        public void update(Book updatedBook){
            for (int i = 0; i < books.size(); i++) {
                if (books.get(i).getId() == updatedBook.getId()) {
                    books.set(i, updatedBook);
                    return;
                }
            }
        }

        // Delete all
        public void deleteAll(){
            books.clear();
        }

        // Find all
        public Iterable<Book> findAll(){
            return books;
        }

        // Find by author
        public Iterable<Book> findByAuthor(String author){
            return books.stream()
                    .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                    .collect(Collectors.toList());
        }

        // Find by title
        public Iterable<Book> findByTitle(String title){
            return books.stream()
                    .filter(book -> book.getTitle().equalsIgnoreCase(title))
                    .collect(Collectors.toList());
        }

        // Sort by title (default ascending)
        public Iterable<Book> sortByTitle(){
            return sortByTitleAsc();
        }

        public Iterable<Book> sortByTitleAsc(){
            return books.stream()
                    .sorted(Comparator.comparing(Book::getTitle))
                    .collect(Collectors.toList());
        }

        public Iterable<Book> sortByTitleDesc(){
            return books.stream()
                    .sorted(Comparator.comparing(Book::getTitle).reversed())
                    .collect(Collectors.toList());
        }
    }

