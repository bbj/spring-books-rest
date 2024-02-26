package com.bbj.books.services.impl;

import com.bbj.books.domain.Book;
import com.bbj.books.domain.BookEntity;
import com.bbj.books.repositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;      // required by BookServiceImpl

    @InjectMocks                                // will inject @Mock previously declared (bookRepository)
    private BookServiceImpl underTest;          // class under test

    @Test
    public void testThatBookIsSaved() {
        final Book book = Book.builder()
                .isbn("02345678")
                .author("Virginia Woolf")
                .title("The Waves")
                .build();

        final BookEntity bookEntity = BookEntity.builder()
                .isbn("02345678")
                .author("Virginia Woolf")
                .title("The Waves")
                .build();

        // tells mockito to return save entity as the one passed to save
        when(bookRepository.save(eq(bookEntity))).thenReturn(bookEntity);

        final Book result = underTest.create(book);
        assertEquals(book, result);
    }
}
