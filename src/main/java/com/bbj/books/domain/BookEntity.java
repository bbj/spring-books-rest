package com.bbj.books.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 									// Lombok getters, setters, equal, hashcode ...
@AllArgsConstructor
@NoArgsConstructor						// required by Spring boot JSON manips
@Builder								// builder pattern to be able to create book.title .... etc
@Entity									// JPA - jakarta.persistence - entity
@Table(name = "books")					// JPA - jakarta.persistence - db table
public class BookEntity {
    @Id									// JPA - jakarta.persistence - uniq ID
    private String isbn;
    private String author;
    private String title;
}