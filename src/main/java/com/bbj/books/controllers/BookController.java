package com.bbj.books.controllers;

import com.bbj.books.domain.Book;
import com.bbj.books.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller                                 // => Spring bean, contains infos on how to expose our Rest endpoint
public class BookController {

    private final BookService bookService;  // interface, not Impl

    @Autowired                              // bookService will be "constructor" injected for us
    public BookController(final BookService bookService) {
        this.bookService = bookService;
    }

    @PutMapping(path = "/books/{isbn}")
    public ResponseEntity<Book> createBook(
            @PathVariable final String isbn,
            @RequestBody final Book book) {
        // instead of Book, we could use another bookDTO class to even more loosely couple the presentation layer
        // but not really necessary here.
        // @RequestBody: tells Spring "when you get this endpoint request, expect a request body
        book.setIsbn(isbn); // make sure the isbn matches
        final Book savedBook = bookService.create(book);
        final ResponseEntity<Book> response = new ResponseEntity<Book>(savedBook, HttpStatus.CREATED);
        return response;
    }
}
