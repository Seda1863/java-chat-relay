package com.example.proje5.libary.repository;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByUserOrderByBorrowDateDesc(User user);
    Optional<BorrowRecord> findByUserAndBookAndReturnDateIsNull(User user, Book book);
}