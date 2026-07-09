import { Service } from '@angular/core';

interface Book {
    id: number;
    title: string;
    author: string;
    price: number;
}

@Service()
export class BookService {
    private books: Book[] = [
        { id: 1, title: 'The Great Gatsby', author: 'F. Scott Fitzgerald', price: 10.99 },
        { id: 2, title: 'To Kill a Mockingbird', author: 'Harper Lee', price: 12.99 },
        { id: 3, title: '1984', author: 'George Orwell', price: 9.99 },
        { id: 4, title: 'Pride and Prejudice', author: 'Jane Austen', price: 11.99 },
        { id: 5, title: 'The Catcher in the Rye', author: 'J.D. Salinger', price: 8.99 }
    ];

    getBooks(): Book[] {
        return this.books;
    }

    getBookById(id: number): Book | undefined {
        return this.books.find(book => book.id === id);
    }

    addBook(book: Book): void {
        this.books.push(book);
    }
}
