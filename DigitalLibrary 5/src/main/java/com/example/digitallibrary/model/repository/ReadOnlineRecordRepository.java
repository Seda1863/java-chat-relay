package com.example.digitallibrary.model.repository;

import jakarta.persistence.EntityManager;
import com.example.digitallibrary.model.entities.ReadOnlineRecord;

import java.util.List;

public class ReadOnlineRecordRepository extends AbstractRepository<ReadOnlineRecord, Long> {

    public ReadOnlineRecordRepository(EntityManager entityManager) {
        super(entityManager, ReadOnlineRecord.class);
    }

    // Kullanıcının çevrim içi okuma kayıtlarını getir
    public List<ReadOnlineRecord> findByUserId(Long userId) {
        return entityManager.createQuery(
                        "SELECT ror FROM ReadOnlineRecord ror WHERE ror.user.id = :userId", ReadOnlineRecord.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}