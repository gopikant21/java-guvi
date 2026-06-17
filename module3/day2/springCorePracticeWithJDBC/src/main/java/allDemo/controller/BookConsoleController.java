package allDemo.controller;

import allDemo.entity.Book;
import allDemo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookConsoleController {

    private BookService service;

    @Autowired
    public BookConsoleController(BookService service) {
        this.service = service;
    }

    public void start() {

        service.saveBook(
                new Book(101,"Spring in Action",
                        "Craig Walls","Programming",
                        799,10));

        service.saveBook(
                new Book(102,"Java Complete Reference",
                        "Herbert Schildt","Programming",
                        950,15));

        service.saveBook(
                new Book(103,"Wings of Fire",
                        "A.P.J Abdul Kalam","Biography",
                        450,20));

        service.saveBook(
                new Book(104,"Clean Code",
                        "Robert Martin","Programming",
                        850,8));

        System.out.println("\nALL BOOKS");
        service.getAllBooks().forEach(System.out::println);

        System.out.println("\nBOOK BY ID");
        System.out.println(service.findBookById(103));

        System.out.println("\nBOOKS BY CATEGORY");
        service.findBooksByCategory("Programming")
                .forEach(System.out::println);

        System.out.println("\nUPDATE PRICE");
        service.updateBookPrice(104, 999);

        System.out.println(service.findBookById(104));

        System.out.println("\nDELETE BOOK");
        service.removeBook(102);

        service.getAllBooks()
                .forEach(System.out::println);

        System.out.println("\nTOTAL BOOKS");
        System.out.println(service.totalBooks());
    }
}
