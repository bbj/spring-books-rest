package com.bbj.books.repositories;

import com.bbj.books.domain.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository        // create an entry in the Spring context => available for later injection
public interface BookRepository extends JpaRepository<BookEntity, String> {

}
