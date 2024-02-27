package com.bbj.books.controllers;

import com.bbj.books.TestData;
import com.bbj.books.domain.Book;
import com.bbj.books.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)    //clean db (new db) before each test method
public class BookControllerIT {

    @Autowired                                  // else => "this.mockMvc" is null
    private MockMvc mockMvc;                    // allow to perform Rest queries using MockMvcRequestBuilders

    @Autowired
    private BookService bookService;            // for testThatRetrieveBookReturnsBookWhenBookExists()

    @Test
    public void testThatBookIsCreatedReturnsHTTP200() throws Exception {
        final Book book = TestData.testBook();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String bookJson = objectMapper.writeValueAsString(book); //JsonProcessingException

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/books/" + book.getIsbn())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bookJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()));
    }

    @Test
    public void testThatBookIsUpdatedReturnsHTTP201() throws Exception {
        final Book book = TestData.testBook();
        bookService.save(book);
        book.setAuthor("Bruno Jullien");

        final ObjectMapper objectMapper = new ObjectMapper();
        final String bookJson = objectMapper.writeValueAsString(book); //JsonProcessingException

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/books/" + book.getIsbn())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bookJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()));
    }

    @Test
    public void testThatRetrieveBookReturns404WhenBookNotFound() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/books/123123123"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatRetrieveBookReturns200WhenBookExists() throws Exception {
        final Book book = TestData.testBook();
        bookService.save(book);
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/books/" + book.getIsbn()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()));
    }

    @Test
    public void testThatListBooksReturnsHttp200EmptyListWhenNoBooksExist() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/books"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    public void testThatListBooksReturnsHttp200AndBooksWhenBooksExist() throws Exception {
        final Book book = TestData.testBook();
        bookService.save(book);
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/books"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].title").value(book.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].author").value(book.getAuthor()));
    }

    @Test
    public void testWhenDeleteThatHttp204IsReturnedWhenBookDoesntExist() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/books/21241341234"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testWhenDeleteThatHttp204IsReturnedWhenExistingBookIsDeleted() throws Exception {
        final Book book = TestData.testBook();
        bookService.save(book);
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/books/" + book.getIsbn()))
                .andExpect(MockMvcResultMatchers.status().isNoContent()); //.isIAmATeapot() to test failure !!!
    }
}