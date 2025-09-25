package com.example.digitallibrary.service;

import jakarta.persistence.EntityManager;
import com.example.digitallibrary.model.entities.Book;
import com.example.digitallibrary.model.entities.BorrowRecord;
import com.example.digitallibrary.model.entities.User;
import com.example.digitallibrary.model.repository.BorrowRecordRepository;

import java.time.LocalDate;
import java.util.List;

public class BorrowService extends BaseService {
    private final BorrowRecordRepository borrowRecordRepository;

    public BorrowService(EntityManager entityManager) {
        super(entityManager);
        this.borrowRecordRepository = new BorrowRecordRepository(entityManager);
    }

    // Kitap ödünç al
    public void borrowBook(User user, Book book) {
        BorrowRecord record = new BorrowRecord(user, book, LocalDate.now());
        borrowRecordRepository.save(record);
    }

    // Kitap iade et
    public void returnBook(BorrowRecord record) {
        record.setReturnDate(LocalDate.now());
        borrowRecordRepository.update(record);
    }

    // Kullanıcının ödünç aldığı kitapları getir
    public List<BorrowRecord> getBorrowedBooks(User user) {
        return borrowRecordRepository.findByUserId(user.getId());
    }
}