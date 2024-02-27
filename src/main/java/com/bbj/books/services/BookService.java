package com.bbj.books.services;

import com.bbj.books.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    boolean isBookExists(Book book);

    Book save(Book book);

    Optional<Book> findById(String isbn);  // Optional: may or may not contain a non-null value

    List<Book> listBooks();

    void deleteBookById(String isbn);
}
