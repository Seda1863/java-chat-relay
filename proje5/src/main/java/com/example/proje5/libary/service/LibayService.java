package com.example.proje5.libary.service;

@Service
@Transactional
public class LibraryService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    public User login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password))
                .orElseThrow(() -> new AuthenticationException("Invalid credentials"));
    }

    public List<Book> searchBooks(String query) {
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                query, query);
    }

    public void borrowBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        BorrowRecord record = new BorrowRecord();
        record.setUser(user);
        record.setBook(book);
        record.setBorrowDate(LocalDateTime.now());
        borrowRecordRepository.save(record);
    }

    public void returnBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        BorrowRecord record = borrowRecordRepository
                .findByUserAndBookAndReturnDateIsNull(user, book)
                .orElseThrow(() -> new IllegalStateException("Book not borrowed"));

        record.setReturnDate(LocalDateTime.now());
        borrowRecordRepository.save(record);
    }

    public String readBookOnline(Long bookId) {
        return bookRepository.findById(bookId)
                .map(Book::getContent)
                .orElseThrow(() -> new NotFoundException("Book not found"));
    }

    public List<BorrowRecord> getBorrowHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return borrowRecordRepository.findByUserOrderByBorrowDateDesc(user);
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Friend not found"));

        user.getFriends().add(friend);
        userRepository.save(user);
    }

    public List<BorrowRecord> getFriendsBorrowHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return user.getFriends().stream()
                .flatMap(friend -> borrowRecordRepository
                        .findByUserOrderByBorrowDateDesc(friend).stream())
                .collect(Collectors.toList());
    }
}