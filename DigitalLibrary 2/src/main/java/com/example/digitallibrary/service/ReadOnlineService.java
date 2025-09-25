package com.example.digitallibrary.service;

import jakarta.persistence.EntityManager;
import com.example.digitallibrary.model.entities.Book;
import com.example.digitallibrary.model.entities.ReadOnlineRecord;
import com.example.digitallibrary.model.entities.User;
import com.example.digitallibrary.model.repository.ReadOnlineRecordRepository;

import java.time.LocalDate;
import java.util.List;

public class ReadOnlineService extends BaseService {
    private final ReadOnlineRecordRepository readOnlineRecordRepository;

    public ReadOnlineService(EntityManager entityManager) {
        super(entityManager);
        this.readOnlineRecordRepository = new ReadOnlineRecordRepository(entityManager);
    }

    // Kullanıcının çevrim içi okuduğu kitapları getir
    public List<ReadOnlineRecord> getOnlineReadings(User user) {
        return readOnlineRecordRepository.findByUserId(user.getId());
    }

    // Çevrim içi okuma kaydı ekle
    public void readBookOnline(User user, Book book) {
        ReadOnlineRecord record = new ReadOnlineRecord(user, book, LocalDate.now());
        readOnlineRecordRepository.save(record);
    }

    // Tüm çevrim içi okuma kayıtlarını getir
    public List<ReadOnlineRecord> getAllOnlineReadings() {
        return readOnlineRecordRepository.findAll();
    }
}