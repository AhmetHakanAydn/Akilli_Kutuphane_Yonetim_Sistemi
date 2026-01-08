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
        System.out.println("Book added successfully: " + book.getTitle());
    }

    /**
     * List all books in the library
     */
    public void listAllBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available in the library.");
            return;
        }
        
        System.out.println("\n========== BOOK LIST ==========");
        for (Book book : books) {
            System.out.println(book);
        }
        System.out.println("Total books: " + books.size());
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
        System.out.println("Book updated successfully.");
    }

    /**
     * Delete a book from the library
     */
    public void deleteBook(int bookId) throws LibraryException {
        databaseManager.deleteBook(bookId);
        books.removeIf(book -> book.getId() == bookId);
        System.out.println("Book deleted successfully.");
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
            System.out.println("No books found matching your search.");
            return;
        }
        
        System.out.println("\n========== SEARCH RESULTS (" + searchType + ") ==========");
        for (Book book : results) {
            System.out.println(book);
        }
        System.out.println("Total results: " + results.size());
    }

    // ==================== STUDENT OPERATIONS ====================

    /**
     * Add a new student
     */
    public void addStudent(Student student) throws LibraryException {
        databaseManager.addStudent(student);
        students.add(student);
        System.out.println("Student added successfully: " + student.getName());
    }

    /**
     * List all students
     */
    public void listAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students registered.");
            return;
        }
        
        System.out.println("\n========== STUDENT LIST ==========");
        for (Student student : students) {
            System.out.println(student);
        }
        System.out.println("Total students: " + students.size());
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
        System.out.println("Student updated successfully.");
    }

    /**
     * Delete a student
     */
    public void deleteStudent(int studentId) throws LibraryException {
        databaseManager.deleteStudent(studentId);
        students.removeIf(student -> student.getId() == studentId);
        System.out.println("Student deleted successfully.");
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
        if (student == null) {
            throw new LibraryException("Student not found with ID: " + studentId);
        }

        // Check if book exists
        Book book = getBookById(bookId);
        if (book == null) {
            throw new LibraryException("Book not found with ID: " + bookId);
        }

        // Check if book is available
        if (!book.isAvailable()) {
            throw new LibraryException("Book is not available: " + book.getTitle());
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

        System.out.println("Book borrowed successfully!");
        System.out.println("Student: " + student.getName());
        System.out.println("Book: " + book.getTitle());
        System.out.println("Return by: " + returnDate);
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
            throw new LibraryException("Active loan not found with ID: " + loanId);
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

        System.out.println("Book returned successfully!");
        if (loan.getPenaltyAmount() > 0) {
            System.out.println("Penalty amount: " + loan.getPenaltyAmount() + " TL");
        } else {
            System.out.println("No penalty - returned on time.");
        }
    }

    /**
     * List all active loans
     */
    public void listActiveLoans() throws LibraryException {
        List<Loan> activeLoans = databaseManager.getActiveLoans();
        
        if (activeLoans.isEmpty()) {
            System.out.println("No active loans.");
            return;
        }

        System.out.println("\n========== ACTIVE LOANS ==========");
        for (Loan loan : activeLoans) {
            Student student = getStudentById(loan.getStudentId());
            Book book = getBookById(loan.getBookId());
            
            System.out.println("Loan ID: " + loan.getId());
            System.out.println("  Student: " + (student != null ? student.getName() : "Unknown"));
            System.out.println("  Book: " + (book != null ? book.getTitle() : "Unknown"));
            System.out.println("  Loan Date: " + loan.getLoanDate());
            System.out.println("  Expected Return: " + loan.getReturnDate());
            
            // Calculate potential penalty if returned today
            double potentialPenalty = loan.calculatePenalty(LocalDate.now());
            if (potentialPenalty > 0) {
                System.out.println("  WARNING: Book is overdue! Potential penalty: " + potentialPenalty + " TL");
            }
            System.out.println("---");
        }
        System.out.println("Total active loans: " + activeLoans.size());
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
            System.out.println("No overdue loans.");
            return;
        }

        System.out.println("\n========== OVERDUE LOANS ==========");
        for (Loan loan : overdueLoans) {
            Student student = getStudentById(loan.getStudentId());
            Book book = getBookById(loan.getBookId());
            double penalty = loan.calculatePenalty(today);
            
            System.out.println("Loan ID: " + loan.getId());
            System.out.println("  Student: " + (student != null ? student.getName() + " (" + student.getStudentNo() + ")" : "Unknown"));
            System.out.println("  Book: " + (book != null ? book.getTitle() : "Unknown"));
            System.out.println("  Expected Return: " + loan.getReturnDate());
            System.out.println("  Days Overdue: " + java.time.temporal.ChronoUnit.DAYS.between(loan.getReturnDate(), today));
            System.out.println("  Current Penalty: " + penalty + " TL");
            System.out.println("---");
        }
        System.out.println("Total overdue loans: " + overdueLoans.size());
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
}
