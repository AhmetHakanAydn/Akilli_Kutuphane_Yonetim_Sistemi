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
            System.out.println("   AKILLI KÜTÜPHANE YÖNETİM SİSTEMİ");
            System.out.println("   SMART LIBRARY MANAGEMENT SYSTEM");
            System.out.println("===========================================\n");

            databaseManager = new DatabaseManager();
            libraryManager = new LibraryManager(databaseManager);

            // Main menu loop
            boolean running = true;
            while (running) {
                try {
                    displayMainMenu();
                    int choice = getIntInput("Seçiminiz: ");

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
                            System.out.println("\nKütüphane Yönetim Sistemini kullandığınız için teşekkür ederiz!");
                            System.out.println("Hoşça kalın!");
                            break;
                        default:
                            System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
                    }
                } catch (LibraryException e) {
                    System.err.println("Hata: " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Beklenmeyen hata: " + e.getMessage());
                }
            }

        } catch (LibraryException e) {
            System.err.println("Sistem başlatılamadı: " + e.getMessage());
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
        System.out.println("\n========== ANA MENÜ ==========");
        System.out.println("1. Kitap İşlemleri");
        System.out.println("2. Öğrenci İşlemleri");
        System.out.println("3. Ödünç Alma İşlemleri");
        System.out.println("4. Çıkış");
        System.out.println("================================");
    }

    /**
     * Book operations menu
     */
    private static void bookOperationsMenu() throws LibraryException {
        boolean back = false;
        while (!back) {
            System.out.println("\n========== KİTAP İŞLEMLERİ ==========");
            System.out.println("1. Kitap Ekle");
            System.out.println("2. Tüm Kitapları Listele");
            System.out.println("3. Kitap Ara");
            System.out.println("4. Kitap Güncelle");
            System.out.println("5. Kitap Sil");
            System.out.println("6. Ana Menüye Dön");
            System.out.println("======================================");

            int choice = getIntInput("Seçiminiz: ");

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
                    System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
            }
        }
    }

    /**
     * Add a new book
     */
    private static void addBook() throws LibraryException {
        System.out.println("\n--- Yeni Kitap Ekle ---");
        
        System.out.print("Kitap başlığını girin: ");
        String title = scanner.nextLine();
        
        System.out.print("Yazar adını girin: ");
        String author = scanner.nextLine();
        
        System.out.print("Kategori girin: ");
        String category = scanner.nextLine();

        Book book = new Book(title, author, category);
        libraryManager.addBook(book);
    }

    /**
     * Search books menu
     */
    private static void searchBooks() {
        System.out.println("\n--- Kitap Ara ---");
        System.out.println("1. Başlığa Göre Ara");
        System.out.println("2. Yazara Göre Ara");
        System.out.println("3. Kategoriye Göre Ara");
        
        int choice = getIntInput("Arama türü: ");

        System.out.print("Arama terimini girin: ");
        String searchTerm = scanner.nextLine();

        List<Book> results;
        String searchType;

        switch (choice) {
            case 1:
                results = libraryManager.searchByTitle(searchTerm);
                searchType = "Başlık: " + searchTerm;
                break;
            case 2:
                results = libraryManager.searchByAuthor(searchTerm);
                searchType = "Yazar: " + searchTerm;
                break;
            case 3:
                results = libraryManager.searchByCategory(searchTerm);
                searchType = "Kategori: " + searchTerm;
                break;
            default:
                System.out.println("Geçersiz arama türü.");
                return;
        }

        libraryManager.displaySearchResults(results, searchType);
    }

    /**
     * Update book information
     */
    private static void updateBook() throws LibraryException {
        System.out.println("\n--- Kitap Güncelle ---");
        libraryManager.listAllBooks();
        
        int bookId = getIntInput("Güncellenecek kitap ID'sini girin: ");
        Book book = libraryManager.getBookById(bookId);
        
        if (book == null) {
            System.out.println("Kitap bulunamadı.");
            return;
        }

        System.out.println("Mevcut kitap bilgisi: " + book);
        System.out.println("Mevcut değeri korumak için boş bırakın.");

        System.out.print("Yeni başlık [" + book.getTitle() + "]: ");
        String title = scanner.nextLine();
        if (!title.trim().isEmpty()) {
            book.setTitle(title);
        }

        System.out.print("Yeni yazar [" + book.getAuthor() + "]: ");
        String author = scanner.nextLine();
        if (!author.trim().isEmpty()) {
            book.setAuthor(author);
        }

        System.out.print("Yeni kategori [" + book.getCategory() + "]: ");
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
        System.out.println("\n--- Kitap Sil ---");
        libraryManager.listAllBooks();
        
        int bookId = getIntInput("Silinecek kitap ID'sini girin: ");
        
        System.out.print("Bu kitabı silmek istediğinizden emin misiniz? (evet/hayır): ");
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("evet")) {
            libraryManager.deleteBook(bookId);
        } else {
            System.out.println("Silme işlemi iptal edildi.");
        }
    }

    /**
     * Student operations menu
     */
    private static void studentOperationsMenu() throws LibraryException {
        boolean back = false;
        while (!back) {
            System.out.println("\n========== ÖĞRENCİ İŞLEMLERİ ==========");
            System.out.println("1. Öğrenci Ekle");
            System.out.println("2. Tüm Öğrencileri Listele");
            System.out.println("3. Öğrenci Güncelle");
            System.out.println("4. Öğrenci Sil");
            System.out.println("5. Ana Menüye Dön");
            System.out.println("=========================================");

            int choice = getIntInput("Seçiminiz: ");

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
                    System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
            }
        }
    }

    /**
     * Add a new student
     */
    private static void addStudent() throws LibraryException {
        System.out.println("\n--- Yeni Öğrenci Ekle ---");
        
        System.out.print("Öğrenci adını girin: ");
        String name = scanner.nextLine();
        
        System.out.print("Öğrenci numarasını girin: ");
        String studentNo = scanner.nextLine();
        
        System.out.print("E-posta adresini girin: ");
        String email = scanner.nextLine();

        Student student = new Student(name, email, studentNo);
        libraryManager.addStudent(student);
    }

    /**
     * Update student information
     */
    private static void updateStudent() throws LibraryException {
        System.out.println("\n--- Öğrenci Güncelle ---");
        libraryManager.listAllStudents();
        
        int studentId = getIntInput("Güncellenecek öğrenci ID'sini girin: ");
        Student student = libraryManager.getStudentById(studentId);
        
        if (student == null) {
            System.out.println("Öğrenci bulunamadı.");
            return;
        }

        System.out.println("Mevcut öğrenci bilgisi: " + student);
        System.out.println("Mevcut değeri korumak için boş bırakın.");

        System.out.print("Yeni ad [" + student.getName() + "]: ");
        String name = scanner.nextLine();
        if (!name.trim().isEmpty()) {
            student.setName(name);
        }

        System.out.print("Yeni öğrenci numarası [" + student.getStudentNo() + "]: ");
        String studentNo = scanner.nextLine();
        if (!studentNo.trim().isEmpty()) {
            student.setStudentNo(studentNo);
        }

        System.out.print("Yeni e-posta [" + student.getEmail() + "]: ");
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
        System.out.println("\n--- Öğrenci Sil ---");
        libraryManager.listAllStudents();
        
        int studentId = getIntInput("Silinecek öğrenci ID'sini girin: ");
        
        System.out.print("Bu öğrenciyi silmek istediğinizden emin misiniz? (evet/hayır): ");
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("evet")) {
            libraryManager.deleteStudent(studentId);
        } else {
            System.out.println("Silme işlemi iptal edildi.");
        }
    }

    /**
     * Loan operations menu
     */
    private static void loanOperationsMenu() throws LibraryException {
        boolean back = false;
        while (!back) {
            System.out.println("\n========== ÖDÜNÇ ALMA İŞLEMLERİ ==========");
            System.out.println("1. Kitap Ödünç Al");
            System.out.println("2. Kitap İade Et");
            System.out.println("3. Aktif Ödünç Listesi");
            System.out.println("4. Geciken Ödünçler");
            System.out.println("5. Ana Menüye Dön");
            System.out.println("==========================================");

            int choice = getIntInput("Seçiminiz: ");

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
                    System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
            }
        }
    }

    /**
     * Borrow a book
     */
    private static void borrowBook() throws LibraryException {
        System.out.println("\n--- Kitap Ödünç Al ---");
        
        libraryManager.listAllStudents();
        int studentId = getIntInput("Öğrenci ID'sini girin: ");
        
        // Show only available books
        System.out.println("\nMüsait kitaplar:");
        List<Book> availableBooks = libraryManager.getAllBooks().stream()
                .filter(Book::isAvailable)
                .collect(java.util.stream.Collectors.toList());
        
        if (availableBooks.isEmpty()) {
            System.out.println("Müsait kitap yok.");
            return;
        }
        
        for (Book book : availableBooks) {
            System.out.println(book);
        }
        
        int bookId = getIntInput("Kitap ID'sini girin: ");
        int loanDays = getIntInput("Ödünç alma süresi (gün): ");
        
        libraryManager.borrowBook(studentId, bookId, loanDays);
    }

    /**
     * Return a book
     */
    private static void returnBook() throws LibraryException {
        System.out.println("\n--- Kitap İade Et ---");
        libraryManager.listActiveLoans();
        
        int loanId = getIntInput("İade edilecek ödünç ID'sini girin: ");
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
                System.out.println("Geçersiz giriş. Lütfen bir sayı girin.");
            }
        }
    }
}
