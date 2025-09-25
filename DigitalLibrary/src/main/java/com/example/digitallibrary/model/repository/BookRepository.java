package com.example.digitallibrary.model.repository;

import jakarta.persistence.EntityManager;
import com.example.digitallibrary.model.entities.Book;

import java.util.List;

public class BookRepository extends AbstractRepository<Book, Long> {

    public BookRepository(EntityManager entityManager) {
        super(entityManager, Book.class);
    }

    public List<Book> searchByTitleOrAuthor(String query) {
        return entityManager.createQuery(
                        "SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(:query) OR LOWER(b.author) LIKE LOWER(:query)", Book.class)
                .setParameter("query", "%" + query.toLowerCase() + "%")
                .getResultList();
    }
}
