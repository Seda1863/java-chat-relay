package com.example.digitallibrary.service;

import jakarta.persistence.EntityManager;
import com.example.digitallibrary.model.entities.Book;
import com.example.digitallibrary.model.repository.BookRepository;

import java.util.List;

public class BookService extends BaseService {
    private final BookRepository bookRepository;

    public BookService(EntityManager entityManager) {
        super(entityManager);
        this.bookRepository = new BookRepository(entityManager);
    }

    // Tüm kitapları getir
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Başlık veya yazara göre kitap ara
    public List<Book> searchBooks(String keyword) {
        return bookRepository.searchByTitleOrAuthor(keyword);
    }

    // Yeni kitap ekle
    public void addBook(Book book) {
        bookRepository.save(book);
    }
}