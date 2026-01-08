package manager;

import exception.LibraryException;
import interfaces.Searchable;
import model.Book;
import model.Loan;
import model.Student;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * LibraryManager handles all library operations and implements Searchable interface.
 * Demonstrates interface implementation, collections, and business logic.
 */
public class LibraryManager implements Searchable {
    private DatabaseManager databaseManager;
    private List<Book> books;
    private List<Student> students;
    private List<Loan> loans;

    /**
     * Constructor - initializes the library manager with database connection
     */
    public LibraryManager(DatabaseManager databaseManager) throws LibraryException {
        this.databaseManager = databaseManager;
        this.books = new ArrayList<>();
        this.students = new ArrayList<>();
        this.loans = new ArrayList<>();
        refreshData();
    }

    /**
     * Refresh data from database
     */
    public void refreshData() throws LibraryException {
        this.books = databaseManager.getAllBooks();
        this.students = databaseManager.getAllStudents();
        this.loans = databaseManager.getAllLoans();
    }

    // ==================== BOOK OPERATIONS ====================

    /**
     * Add a new book to the library
     */
    public void addBook(Book book) throws LibraryException {
        databaseManager.addBook(book);
        books.add(book);
        System.out.println("Kitap başarıyla eklendi: " + book.getTitle());
    }

    /**
     * List all books in the library
     */
    public void listAllBooks() {
        if (books.isEmpty()) {
            System.out.println("Kütüphanede kitap bulunmuyor.");
            return;
        }
        
        System.out.println("\n========== KİTAP LİSTESİ ==========");
        for (Book book : books) {
            System.out.println(book);
        }
        System.out.println("Toplam kitap: " + books.size());
    }

    /**
     * Update book information
     */
    public void updateBook(Book book) throws LibraryException {
        databaseManager.updateBook(book);
        // Update in local list
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == book.getId()) {
                books.set(i, book);
                break;
            }
        }
        System.out.println("Kitap başarıyla güncellendi.");
    }

    /**
     * Delete a book from the library
     */
    public void deleteBook(int bookId) throws LibraryException {
        databaseManager.deleteBook(bookId);
        books.removeIf(book -> book.getId() == bookId);
        System.out.println("Kitap başarıyla silindi.");
    }

    /**
     * Get book by ID
     */
    public Book getBookById(int bookId) throws LibraryException {
        return databaseManager.getBookById(bookId);
    }

    // ==================== SEARCH OPERATIONS (Interface Implementation) ====================

    /**
     * Search books by title (implements Searchable interface)
     */
    @Override
    public List<Book> searchByTitle(String title) {
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Search books by author (implements Searchable interface)
     */
    @Override
    public List<Book> searchByAuthor(String author) {
        return books.stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Search books by category (implements Searchable interface)
     */
    @Override
    public List<Book> searchByCategory(String category) {
        return books.stream()
                .filter(book -> book.getCategory() != null && 
                        book.getCategory().toLowerCase().contains(category.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Display search results
     */
    public void displaySearchResults(List<Book> results, String searchType) {
        if (results.isEmpty()) {
            System.out.println("Aramanıza uygun kitap bulunamadı.");
            return;
        }
        
        System.out.println("\n========== ARAMA SONUÇLARI (" + searchType + ") ==========");
        for (Book book : results) {
            System.out.println(book);
        }
        System.out.println("Toplam sonuç: " + results.size());
    }

    // ==================== STUDENT OPERATIONS ====================

    /**
     * Add a new student
     */
    public void addStudent(Student student) throws LibraryException {
        databaseManager.addStudent(student);
        students.add(student);
        System.out.println("Öğrenci başarıyla eklendi: " + student.getName());
    }

    /**
     * List all students
     */
    public void listAllStudents() {
        if (students.isEmpty()) {
            System.out.println("Kayıtlı öğrenci bulunmuyor.");
            return;
        }
        
        System.out.println("\n========== ÖĞRENCİ LİSTESİ ==========");
        for (Student student : students) {
            System.out.println(student);
        }
        System.out.println("Toplam öğrenci: " + students.size());
    }

    /**
     * Update student information
     */
    public void updateStudent(Student student) throws LibraryException {
        databaseManager.updateStudent(student);
        // Update in local list
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId() == student.getId()) {
                students.set(i, student);
                break;
            }
        }
        System.out.println("Öğrenci başarıyla güncellendi.");
    }

    /**
     * Delete a student
     */
    public void deleteStudent(int studentId) throws LibraryException {
        databaseManager.deleteStudent(studentId);
        students.removeIf(student -> student.getId() == studentId);
        System.out.println("Öğrenci başarıyla silindi.");
    }

    /**
     * Get student by ID
     */
    public Student getStudentById(int studentId) throws LibraryException {
        return databaseManager.getStudentById(studentId);
    }

    // ==================== LOAN OPERATIONS ====================

    /**
     * Borrow a book - create a new loan
     */
    public void borrowBook(int studentId, int bookId, int loanDays) throws LibraryException {
        // Check if student exists
        Student student = getStudentById(studentId);
        validateNotNull(student, "Student", studentId);

        // Check if book exists
        Book book = getBookById(bookId);
        validateNotNull(book, "Book", bookId);

        // Check if book is available
        if (!book.isAvailable()) {
            throw new LibraryException("Kitap müsait değil: " + book.getTitle());
        }

        // Create loan
        LocalDate loanDate = LocalDate.now();
        LocalDate returnDate = loanDate.plusDays(loanDays);
        Loan loan = new Loan(studentId, bookId, loanDate, returnDate);
        
        databaseManager.addLoan(loan);
        loans.add(loan);

        // Update book availability
        book.setAvailable(false);
        databaseManager.updateBook(book);

        System.out.println("Kitap başarıyla ödünç alındı!");
        System.out.println("Öğrenci: " + student.getName());
        System.out.println("Kitap: " + book.getTitle());
        System.out.println("İade tarihi: " + returnDate);
    }

    /**
     * Return a book
     */
    public void returnBook(int loanId) throws LibraryException {
        // Find the loan
        Loan loan = null;
        for (Loan l : loans) {
            if (l.getId() == loanId && l.getActualReturnDate() == null) {
                loan = l;
                break;
            }
        }

        if (loan == null) {
            throw new LibraryException("Aktif ödünç bulunamadı, ID: " + loanId);
        }

        // Set actual return date and calculate penalty
        loan.setActualReturnDate(LocalDate.now());
        loan.calculatePenalty();

        // Update loan in database
        databaseManager.updateLoan(loan);

        // Make book available again
        Book book = getBookById(loan.getBookId());
        if (book != null) {
            book.setAvailable(true);
            databaseManager.updateBook(book);
        }

        System.out.println("Kitap başarıyla iade edildi!");
        if (loan.getPenaltyAmount() > 0) {
            System.out.println("Ceza miktarı: " + loan.getPenaltyAmount() + " TL");
        } else {
            System.out.println("Ceza yok - zamanında iade edildi.");
        }
    }

    /**
     * List all active loans
     */
    public void listActiveLoans() throws LibraryException {
        List<Loan> activeLoans = databaseManager.getActiveLoans();
        
        if (activeLoans.isEmpty()) {
            System.out.println("Aktif ödünç yok.");
            return;
        }

        System.out.println("\n========== AKTİF ÖDÜNÇLER ==========");
        for (Loan loan : activeLoans) {
            Student student = getStudentById(loan.getStudentId());
            Book book = getBookById(loan.getBookId());
            
            System.out.println("Ödünç ID: " + loan.getId());
            System.out.println("  Öğrenci: " + (student != null ? student.getName() : "Bilinmiyor"));
            System.out.println("  Kitap: " + (book != null ? book.getTitle() : "Bilinmiyor"));
            System.out.println("  Ödünç Tarihi: " + loan.getLoanDate());
            System.out.println("  Beklenen İade: " + loan.getReturnDate());
            
            // Calculate potential penalty if returned today
            double potentialPenalty = loan.calculatePenalty(LocalDate.now());
            if (potentialPenalty > 0) {
                System.out.println("  UYARI: Kitap gecikmiş! Olası ceza: " + potentialPenalty + " TL");
            }
            System.out.println("---");
        }
        System.out.println("Toplam aktif ödünç: " + activeLoans.size());
    }

    /**
     * List overdue books (late returns)
     */
    public void listOverdueLoans() throws LibraryException {
        List<Loan> activeLoans = databaseManager.getActiveLoans();
        LocalDate today = LocalDate.now();
        
        List<Loan> overdueLoans = activeLoans.stream()
                .filter(loan -> loan.getReturnDate().isBefore(today))
                .collect(Collectors.toList());

        if (overdueLoans.isEmpty()) {
            System.out.println("Geciken ödünç yok.");
            return;
        }

        System.out.println("\n========== GECİKEN ÖDÜNÇLER ==========");
        for (Loan loan : overdueLoans) {
            Student student = getStudentById(loan.getStudentId());
            Book book = getBookById(loan.getBookId());
            double penalty = loan.calculatePenalty(today);
            
            System.out.println("Ödünç ID: " + loan.getId());
            System.out.println("  Öğrenci: " + (student != null ? student.getName() + " (" + student.getStudentNo() + ")" : "Bilinmiyor"));
            System.out.println("  Kitap: " + (book != null ? book.getTitle() : "Bilinmiyor"));
            System.out.println("  Beklenen İade: " + loan.getReturnDate());
            System.out.println("  Gecikme Günü: " + java.time.temporal.ChronoUnit.DAYS.between(loan.getReturnDate(), today));
            System.out.println("  Güncel Ceza: " + penalty + " TL");
            System.out.println("---");
        }
        System.out.println("Toplam geciken ödünç: " + overdueLoans.size());
    }

    /**
     * Get all loans
     */
    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }

    /**
     * Get all books
     */
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    /**
     * Get all students
     */
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    // ==================== HELPER METHODS ====================

    /**
     * Validate that an entity is not null and throw exception if it is
     */
    private void validateNotNull(Object entity, String entityType, int id) throws LibraryException {
        if (entity == null) {
            throw new LibraryException(entityType + " bulunamadı, ID: " + id);
        }
    }
}
