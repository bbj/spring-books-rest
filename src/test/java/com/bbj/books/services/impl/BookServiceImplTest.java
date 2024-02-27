package com.bbj.books.services.impl;

import com.bbj.books.TestData;
import com.bbj.books.domain.Book;
import com.bbj.books.domain.BookEntity;
import com.bbj.books.repositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;      // required by BookServiceImpl

    @InjectMocks                                // will inject @Mock previously declared (bookRepository)
    private BookServiceImpl underTest;          // class under test

    @Test
    public void testThatBookIsSaved() {
        final Book book = TestData.testBook();
        final BookEntity bookEntity = TestData.testBookEntity();

        // tells mockito to return save entity as the one passed to save
        when(bookRepository.save(eq(bookEntity))).thenReturn(bookEntity);

        final Book result = underTest.save(book);
        assertEquals(book, result);
    }

    @Test
    public void testThatFindByIdReturnsEmptyWhenNoBook() {
        final String isbn = "123123123";
        when(bookRepository.findById(eq(isbn))).thenReturn(Optional.empty());

        final Optional<Book> result = underTest.findById(isbn);
        //assertNull(result); //fails: Expected :null    Actual   :Optional.empty
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testThatFindByIdReturnsBookWhenExists() {
        final Book book = TestData.testBook();
        final BookEntity bookEntity = TestData.testBookEntity();
        when(bookRepository.findById(eq(book.getIsbn()))).thenReturn(Optional.of(bookEntity));

        final Optional<Book> result = underTest.findById(book.getIsbn());
        //assertNull(result); //fails: Expected :null    Actual   :Optional.empty
        assertEquals(Optional.of(book), result);
    }

    @Test
    public void testListBooksReturnsEmptyListWhenNoBooksExist() {
        final List<Book> result = underTest.listBooks();
        assertEquals(0, result.size());
    }

    @Test
    public void testListBooksReturnsBooksWhenExist() {
        final BookEntity bookEntity = TestData.testBookEntity();
        when(bookRepository.findAll()).thenReturn(List.of(bookEntity));
        final List<Book> result = underTest.listBooks();
        assertEquals(1, result.size());
    }

    @Test
    public void testBookExistsReturnsFalseWhenBookDoesntExist() {
        when(bookRepository.existsById(any())).thenReturn(false);
        final boolean result = underTest.isBookExists(TestData.testBook());
        assertFalse(result);
    }

    @Test
    public void testBookExistsReturnsTrueWhenBookExists() {
        when(bookRepository.existsById(any())).thenReturn(true);
        final boolean result = underTest.isBookExists(TestData.testBook());
        assertTrue(result);
    }

    @Test
    public void testDeleteBookDeletesBook() {
        final String isbn = "1221212121212";
        underTest.deleteBookById(isbn);
        //verify the Mock bookRepository.deleteById(isbn) was called 1 time
        verify(bookRepository, times(1)).deleteById(eq(isbn));
    }
}
