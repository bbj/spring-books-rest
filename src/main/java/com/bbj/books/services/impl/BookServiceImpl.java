package com.bbj.books.services.impl;

import com.bbj.books.domain.Book;
import com.bbj.books.domain.BookEntity;
import com.bbj.books.repositories.BookRepository;
import com.bbj.books.services.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// @Service: put it in the Spring Context => every time a BookService impl is needed,
//           it will be injected
@Service
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository; // final: good practice as never re-assigned after constructed

    @Autowired // tells Spring to inject constructor params if found in context
    public BookServiceImpl(final BookRepository bookRepository) { // final: good practice as not reassigned
        this.bookRepository = bookRepository;
    }

    @Override
    public boolean isBookExists(Book book) {
        return bookRepository.existsById(book.getIsbn());
    }

    @Override
    public Book save(final Book book) { // [final]: never re-assigned
        final BookEntity bookEntity = bookToBookEntity(book);
        final BookEntity savedBE = bookRepository.save(bookEntity);
        return bookEntityToBook(savedBE);
    }

    @Override
    public Optional<Book> findById(String isbn) {
        final Optional<BookEntity> foundBook = bookRepository.findById(isbn);
        return foundBook.map(this::bookEntityToBook);
                                        // this == BookServiceImpl
                                        // map() transform object if present into Optional<Book>
                                        // because BookServiceImpl::bookEntityToBook() returns a Book
    }

    @Override
    public List<Book> listBooks() {
        final List<BookEntity> foundBooks = bookRepository.findAll();
        return foundBooks.stream().map(this::bookEntityToBook).collect(Collectors.toList());
    }

    @Override
    public void deleteBookById(String isbn) {
        try {
            bookRepository.deleteById(isbn); // will raise an exception if does not exist
        } catch (EmptyResultDataAccessException ex) {
            log.debug("Attempted to delete non-existent book", ex);
        }
    }

    private BookEntity bookToBookEntity(Book book) {
        return BookEntity.builder()
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .build();
    }

    private Book bookEntityToBook(BookEntity bookEntity) {
        return Book.builder()
                .isbn(bookEntity.getIsbn())
                .title(bookEntity.getTitle())
                .author(bookEntity.getAuthor())
                .build();
    }
}

/*
    Alternative to @Autowired on constructor:
        @Autowired
        private BookRepository bookRepository;

        this will work too
        but:
            - not final anymore, can be re-assigned
            - not recommended in Spring documentation
 */