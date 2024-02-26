package com.bbj.books.services.impl;

import com.bbj.books.domain.Book;
import com.bbj.books.domain.BookEntity;
import com.bbj.books.repositories.BookRepository;
import com.bbj.books.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// @Service: put it in the Spring Context => every time a BookService impl is needed,
//           it will be injected
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository; // final: good practice as never re-assigned after constructed

    @Autowired // tells Spring to inject constructor params if found in context
    public BookServiceImpl(final BookRepository bookRepository) { // final: good practice as not reassigned
        this.bookRepository = bookRepository;
    }

    @Override
    public Book create(final Book book) { // [final]: never re-assigned
        final BookEntity bookEntity = bookToBookEntity(book);
        final BookEntity savedBE = bookRepository.save(bookEntity);
        return bookEntityToBook(savedBE);
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