package com.example.proje5.libary.model;

public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private String content;  // For online reading

    @OneToMany(mappedBy = "book")
    private List<BorrowRecord> borrowRecords = new ArrayList<>();
}
