import exception.LibraryException;
import manager.DatabaseManager;
import manager.LibraryManager;
import model.Book;
import model.Student;

import java.util.List;
import java.util.Scanner;

/**
 * Main class - Entry point for the Library Management System.
 * Demonstrates console-based menu system with switch-case statements.
 */
public class Main {
    private static LibraryManager libraryManager;
    private static Scanner scanner;
    private static DatabaseManager databaseManager;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        try {
            // Initialize database and library manager
            System.out.println("===========================================");
            System.out.println("   SMART LIBRARY MANAGEMENT SYSTEM");
            System.out.println("   Akıllı Kütüphane Yönetim Sistemi");
            System.out.println("===========================================\n");

            databaseManager = new DatabaseManager();
            libraryManager = new LibraryManager(databaseManager);

            // Main menu loop
            boolean running = true;
            while (running) {
                try {
                    displayMainMenu();
                    int choice = getIntInput("Enter your choice: ");

                    switch (choice) {
                        case 1:
                            bookOperationsMenu();
                            break;
                        case 2:
                            studentOperationsMenu();
                            break;
                        case 3:
                            loanOperationsMenu();
                            break;
                        case 4:
                            running = false;
                            System.out.println("\nThank you for using the Library Management System!");
                            System.out.println("Goodbye!");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } catch (LibraryException e) {
                    System.err.println("Error: " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Unexpected error: " + e.getMessage());
                }
            }

        } catch (LibraryException e) {
            System.err.println("Failed to initialize system: " + e.getMessage());
        } finally {
            // Clean up resources
            if (databaseManager != null) {
                databaseManager.closeConnection();
            }
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    /**
     * Display main menu
     */
    private static void displayMainMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. Book Operations");
        System.out.println("2. Student Operations");
        System.out.println("3. Loan Operations");
        System.out.println("4. Exit");
        System.out.println("================================");
    }

    /**
     * Book operations menu
     */
    private static void bookOperationsMenu() throws LibraryException {
        boolean back = false;
        while (!back) {
            System.out.println("\n========== BOOK OPERATIONS ==========");
            System.out.println("1. Add Book");
            System.out.println("2. List All Books");
            System.out.println("3. Search Books");
            System.out.println("4. Update Book");
            System.out.println("5. Delete Book");
            System.out.println("6. Back to Main Menu");
            System.out.println("======================================");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    libraryManager.listAllBooks();
                    break;
                case 3:
                    searchBooks();
                    break;
                case 4:
                    updateBook();
                    break;
                case 5:
                    deleteBook();
                    break;
                case 6:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Add a new book
     */
    private static void addBook() throws LibraryException {
        System.out.println("\n--- Add New Book ---");
        
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        
        System.out.print("Enter author name: ");
        String author = scanner.nextLine();
        
        System.out.print("Enter category: ");
        String category = scanner.nextLine();

        Book book = new Book(title, author, category);
        libraryManager.addBook(book);
    }

    /**
     * Search books menu
     */
    private static void searchBooks() {
        System.out.println("\n--- Search Books ---");
        System.out.println("1. Search by Title");
        System.out.println("2. Search by Author");
        System.out.println("3. Search by Category");
        
        int choice = getIntInput("Enter search type: ");

        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine();

        List<Book> results;
        String searchType;

        switch (choice) {
            case 1:
                results = libraryManager.searchByTitle(searchTerm);
                searchType = "Title: " + searchTerm;
                break;
            case 2:
                results = libraryManager.searchByAuthor(searchTerm);
                searchType = "Author: " + searchTerm;
                break;
            case 3:
                results = libraryManager.searchByCategory(searchTerm);
                searchType = "Category: " + searchTerm;
                break;
            default:
                System.out.println("Invalid search type.");
                return;
        }

        libraryManager.displaySearchResults(results, searchType);
    }

    /**
     * Update book information
     */
    private static void updateBook() throws LibraryException {
        System.out.println("\n--- Update Book ---");
        libraryManager.listAllBooks();
        
        int bookId = getIntInput("Enter book ID to update: ");
        Book book = libraryManager.getBookById(bookId);
        
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        System.out.println("Current book info: " + book);
        System.out.println("Leave blank to keep current value.");

        System.out.print("Enter new title [" + book.getTitle() + "]: ");
        String title = scanner.nextLine();
        if (!title.trim().isEmpty()) {
            book.setTitle(title);
        }

        System.out.print("Enter new author [" + book.getAuthor() + "]: ");
        String author = scanner.nextLine();
        if (!author.trim().isEmpty()) {
            book.setAuthor(author);
        }

        System.out.print("Enter new category [" + book.getCategory() + "]: ");
        String category = scanner.nextLine();
        if (!category.trim().isEmpty()) {
            book.setCategory(category);
        }

        libraryManager.updateBook(book);
    }

    /**
     * Delete a book
     */
    private static void deleteBook() throws LibraryException {
        System.out.println("\n--- Delete Book ---");
        libraryManager.listAllBooks();
        
        int bookId = getIntInput("Enter book ID to delete: ");
        
        System.out.print("Are you sure you want to delete this book? (yes/no): ");
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("yes")) {
            libraryManager.deleteBook(bookId);
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    /**
     * Student operations menu
     */
    private static void studentOperationsMenu() throws LibraryException {
        boolean back = false;
        while (!back) {
            System.out.println("\n========== STUDENT OPERATIONS ==========");
            System.out.println("1. Add Student");
            System.out.println("2. List All Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Back to Main Menu");
            System.out.println("=========================================");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    libraryManager.listAllStudents();
                    break;
                case 3:
                    updateStudent();
                    break;
                case 4:
                    deleteStudent();
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Add a new student
     */
    private static void addStudent() throws LibraryException {
        System.out.println("\n--- Add New Student ---");
        
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter student number: ");
        String studentNo = scanner.nextLine();
        
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        Student student = new Student(name, email, studentNo);
        libraryManager.addStudent(student);
    }

    /**
     * Update student information
     */
    private static void updateStudent() throws LibraryException {
        System.out.println("\n--- Update Student ---");
        libraryManager.listAllStudents();
        
        int studentId = getIntInput("Enter student ID to update: ");
        Student student = libraryManager.getStudentById(studentId);
        
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("Current student info: " + student);
        System.out.println("Leave blank to keep current value.");

        System.out.print("Enter new name [" + student.getName() + "]: ");
        String name = scanner.nextLine();
        if (!name.trim().isEmpty()) {
            student.setName(name);
        }

        System.out.print("Enter new student number [" + student.getStudentNo() + "]: ");
        String studentNo = scanner.nextLine();
        if (!studentNo.trim().isEmpty()) {
            student.setStudentNo(studentNo);
        }

        System.out.print("Enter new email [" + student.getEmail() + "]: ");
        String email = scanner.nextLine();
        if (!email.trim().isEmpty()) {
            student.setEmail(email);
        }

        libraryManager.updateStudent(student);
    }

    /**
     * Delete a student
     */
    private static void deleteStudent() throws LibraryException {
        System.out.println("\n--- Delete Student ---");
        libraryManager.listAllStudents();
        
        int studentId = getIntInput("Enter student ID to delete: ");
        
        System.out.print("Are you sure you want to delete this student? (yes/no): ");
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("yes")) {
            libraryManager.deleteStudent(studentId);
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    /**
     * Loan operations menu
     */
    private static void loanOperationsMenu() throws LibraryException {
        boolean back = false;
        while (!back) {
            System.out.println("\n========== LOAN OPERATIONS ==========");
            System.out.println("1. Borrow Book");
            System.out.println("2. Return Book");
            System.out.println("3. List Active Loans");
            System.out.println("4. List Overdue Loans");
            System.out.println("5. Back to Main Menu");
            System.out.println("======================================");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    borrowBook();
                    break;
                case 2:
                    returnBook();
                    break;
                case 3:
                    libraryManager.listActiveLoans();
                    break;
                case 4:
                    libraryManager.listOverdueLoans();
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Borrow a book
     */
    private static void borrowBook() throws LibraryException {
        System.out.println("\n--- Borrow Book ---");
        
        libraryManager.listAllStudents();
        int studentId = getIntInput("Enter student ID: ");
        
        // Show only available books
        System.out.println("\nAvailable books:");
        List<Book> availableBooks = libraryManager.getAllBooks().stream()
                .filter(Book::isAvailable)
                .collect(java.util.stream.Collectors.toList());
        
        if (availableBooks.isEmpty()) {
            System.out.println("No available books.");
            return;
        }
        
        for (Book book : availableBooks) {
            System.out.println(book);
        }
        
        int bookId = getIntInput("Enter book ID: ");
        int loanDays = getIntInput("Enter loan duration (days): ");
        
        libraryManager.borrowBook(studentId, bookId, loanDays);
    }

    /**
     * Return a book
     */
    private static void returnBook() throws LibraryException {
        System.out.println("\n--- Return Book ---");
        libraryManager.listActiveLoans();
        
        int loanId = getIntInput("Enter loan ID to return: ");
        libraryManager.returnBook(loanId);
    }

    /**
     * Get integer input with error handling
     */
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}
