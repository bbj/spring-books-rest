package com.bbj.books.controllers;

import com.bbj.books.domain.Book;
import com.bbj.books.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController                             // => Spring bean, contains infos on how to expose our Rest endpoint
public class BookController {

    private final BookService bookService;  // interface, not Impl

    @Autowired                              // bookService will be "constructor" injected for us
    public BookController(final BookService bookService) {
        this.bookService = bookService;
    }

    @PutMapping(path = "/books/{isbn}")
    public ResponseEntity<Book> createUpdateBook(
            @PathVariable final String isbn,
            @RequestBody final Book book) {
        // instead of Book, we could use another bookDTO class to even more loosely couple the presentation layer
        // but not really necessary here.
        // @RequestBody: tells Spring "when you get this endpoint request, expect a request body
        book.setIsbn(isbn); // make sure the isbn matches
        final boolean isBookExists = bookService.isBookExists(book);
        final Book savedBook = bookService.save(book);

        if (isBookExists) {
            return new ResponseEntity<Book>(savedBook, HttpStatus.OK); //200
        } else {
            return new ResponseEntity<Book>(savedBook, HttpStatus.CREATED); //201
        }
    }

    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<Book> retrieveBook(
            @PathVariable final String isbn
    ) {
        final Optional<Book> foundBook = bookService.findById(isbn);
        return foundBook
                .map(book -> new ResponseEntity<Book>(book, HttpStatus.OK))
                .orElse(new ResponseEntity<Book>(HttpStatus.NOT_FOUND)); //404
    }

    @GetMapping(path = "/books")
    public ResponseEntity<List<Book>> listBook() {
        return new ResponseEntity<List<Book>>(bookService.listBooks(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/books/{isbn}")
    public ResponseEntity deleteBook(                   //raw use of ResponseEntity<>, as body empty
            @PathVariable final String isbn
    ) {
        bookService.deleteBookById(isbn);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); //204
    }
}
