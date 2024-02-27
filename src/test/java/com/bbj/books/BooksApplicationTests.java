package com.bbj.books;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest                         // good for integration tests; starts a real Spring server
class BooksApplicationTests {

    @Test                               // does nothing but at least load Spring context
    void contextLoads() {
    }

}
