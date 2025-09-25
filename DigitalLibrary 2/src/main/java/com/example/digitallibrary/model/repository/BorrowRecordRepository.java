package com.example.digitallibrary.model.repository;

import jakarta.persistence.EntityManager;
import com.example.digitallibrary.model.entities.BorrowRecord;

import java.util.List;

public class BorrowRecordRepository extends AbstractRepository<BorrowRecord, Long> {

    public BorrowRecordRepository(EntityManager entityManager) {
        super(entityManager, BorrowRecord.class);
    }

    // Kullanıcıya ait ödünç alma kayıtlarını getir
    public List<BorrowRecord> findByUserId(Long userId) {
        return entityManager.createQuery(
                        "SELECT br FROM BorrowRecord br WHERE br.user.id = :userId", BorrowRecord.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}