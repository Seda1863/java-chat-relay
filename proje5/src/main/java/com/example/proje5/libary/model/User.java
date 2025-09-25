package com.example.proje5.libary.model;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    @ManyToMany
    private Set<User> friends = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<BorrowRecord> borrowHistory = new ArrayList<>();
}