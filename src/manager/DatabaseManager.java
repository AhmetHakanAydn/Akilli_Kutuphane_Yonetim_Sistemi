package manager;

import exception.LibraryException;
import model.Book;
import model.Loan;
import model.Student;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseManager handles all database operations using JDBC.
 * Demonstrates JDBC connectivity and CRUD operations.
 */
public class DatabaseManager {
    private Connection connection;
    private static final String DB_URL = "jdbc:sqlite:library.db";

    /**
     * Constructor - establishes database connection
     */
    public DatabaseManager() throws LibraryException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Veritabanı bağlantısı başarıyla kuruldu.");
            initializeDatabase();
        } catch (ClassNotFoundException | SQLException e) {
            throw new LibraryException("Veritabanına bağlanılamadı", e);
        }
    }

    /**
     * Initialize database tables if they don't exist
     */
    private void initializeDatabase() throws LibraryException {
        try {
            Statement stmt = connection.createStatement();
            
            // Create students table
            String createStudentsTable = "CREATE TABLE IF NOT EXISTS students (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name VARCHAR(100) NOT NULL," +
                    "student_no VARCHAR(20) UNIQUE NOT NULL," +
                    "email VARCHAR(100))";
            stmt.execute(createStudentsTable);

            // Create books table
            String createBooksTable = "CREATE TABLE IF NOT EXISTS books (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title VARCHAR(200) NOT NULL," +
                    "author VARCHAR(100) NOT NULL," +
                    "category VARCHAR(50)," +
                    "is_available BOOLEAN DEFAULT TRUE)";
            stmt.execute(createBooksTable);

            // Create loans table
            String createLoansTable = "CREATE TABLE IF NOT EXISTS loans (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "student_id INTEGER NOT NULL," +
                    "book_id INTEGER NOT NULL," +
                    "loan_date DATE NOT NULL," +
                    "return_date DATE NOT NULL," +
                    "actual_return_date DATE," +
                    "penalty_amount DECIMAL(10,2) DEFAULT 0," +
                    "FOREIGN KEY (student_id) REFERENCES students(id)," +
                    "FOREIGN KEY (book_id) REFERENCES books(id))";
            stmt.execute(createLoansTable);

            stmt.close();
            System.out.println("Veritabanı tabloları başarıyla başlatıldı.");
        } catch (SQLException e) {
            throw new LibraryException("Veritabanı başlatılamadı", e);
        }
    }

    // ==================== BOOK CRUD OPERATIONS ====================

    /**
     * CREATE - Add a new book to the database
     */
    public void addBook(Book book) throws LibraryException {
        String sql = "INSERT INTO books (title, author, category, is_available) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getCategory());
            pstmt.setBoolean(4, book.isAvailable());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    book.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new LibraryException("Kitap eklenemedi", e);
        }
    }

    /**
     * READ - Get all books from the database
     */
    public List<Book> getAllBooks() throws LibraryException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Book book = new Book(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("category"),
                    rs.getBoolean("is_available")
                );
                books.add(book);
            }
        } catch (SQLException e) {
            throw new LibraryException("Kitaplar alınamadı", e);
        }
        return books;
    }

    /**
     * READ - Get book by ID
     */
    public Book getBookById(int id) throws LibraryException {
        String sql = "SELECT * FROM books WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Book(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("category"),
                    rs.getBoolean("is_available")
                );
            }
        } catch (SQLException e) {
            throw new LibraryException("Kitap alınamadı", e);
        }
        return null;
    }

    /**
     * UPDATE - Update book information
     */
    public void updateBook(Book book) throws LibraryException {
        String sql = "UPDATE books SET title = ?, author = ?, category = ?, is_available = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getCategory());
            pstmt.setBoolean(4, book.isAvailable());
            pstmt.setInt(5, book.getId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new LibraryException("Kitap bulunamadı, ID: " + book.getId());
            }
        } catch (SQLException e) {
            throw new LibraryException("Kitap güncellenemedi", e);
        }
    }

    /**
     * DELETE - Delete a book from the database
     */
    public void deleteBook(int id) throws LibraryException {
        // First check if book is currently on loan
        String checkSql = "SELECT COUNT(*) FROM loans WHERE book_id = ? AND actual_return_date IS NULL";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                throw new LibraryException("Kitap silinemez - şu anda ödünç verilmiş");
            }
        } catch (SQLException e) {
            throw new LibraryException("Kitap ödünç durumu kontrol edilemedi", e);
        }

        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new LibraryException("Kitap bulunamadı, ID: " + id);
            }
        } catch (SQLException e) {
            throw new LibraryException("Kitap silinemedi", e);
        }
    }

    // ==================== STUDENT CRUD OPERATIONS ====================

    /**
     * CREATE - Add a new student to the database
     */
    public void addStudent(Student student) throws LibraryException {
        String sql = "INSERT INTO students (name, student_no, email) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getStudentNo());
            pstmt.setString(3, student.getEmail());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    student.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new LibraryException("Öğrenci numarası zaten mevcut: " + student.getStudentNo());
            }
            throw new LibraryException("Öğrenci eklenemedi", e);
        }
    }

    /**
     * READ - Get all students from the database
     */
    public List<Student> getAllStudents() throws LibraryException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Student student = new Student(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("student_no")
                );
                students.add(student);
            }
        } catch (SQLException e) {
            throw new LibraryException("Öğrenciler alınamadı", e);
        }
        return students;
    }

    /**
     * READ - Get student by ID
     */
    public Student getStudentById(int id) throws LibraryException {
        String sql = "SELECT * FROM students WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Student(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("student_no")
                );
            }
        } catch (SQLException e) {
            throw new LibraryException("Öğrenci alınamadı", e);
        }
        return null;
    }

    /**
     * UPDATE - Update student information
     */
    public void updateStudent(Student student) throws LibraryException {
        String sql = "UPDATE students SET name = ?, student_no = ?, email = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getStudentNo());
            pstmt.setString(3, student.getEmail());
            pstmt.setInt(4, student.getId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new LibraryException("Öğrenci bulunamadı, ID: " + student.getId());
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new LibraryException("Öğrenci numarası zaten mevcut: " + student.getStudentNo());
            }
            throw new LibraryException("Öğrenci güncellenemedi", e);
        }
    }

    /**
     * DELETE - Delete a student from the database
     */
    public void deleteStudent(int id) throws LibraryException {
        // First check if student has active loans
        String checkSql = "SELECT COUNT(*) FROM loans WHERE student_id = ? AND actual_return_date IS NULL";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                throw new LibraryException("Öğrenci silinemez - aktif ödünç kaydı var");
            }
        } catch (SQLException e) {
            throw new LibraryException("Öğrenci ödünç durumu kontrol edilemedi", e);
        }

        String sql = "DELETE FROM students WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new LibraryException("Öğrenci bulunamadı, ID: " + id);
            }
        } catch (SQLException e) {
            throw new LibraryException("Öğrenci silinemedi", e);
        }
    }

    // ==================== LOAN CRUD OPERATIONS ====================

    /**
     * CREATE - Add a new loan record
     */
    public void addLoan(Loan loan) throws LibraryException {
        String sql = "INSERT INTO loans (student_id, book_id, loan_date, return_date, actual_return_date, penalty_amount) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, loan.getStudentId());
            pstmt.setInt(2, loan.getBookId());
            pstmt.setDate(3, Date.valueOf(loan.getLoanDate()));
            pstmt.setDate(4, Date.valueOf(loan.getReturnDate()));
            pstmt.setDate(5, loan.getActualReturnDate() != null ? Date.valueOf(loan.getActualReturnDate()) : null);
            pstmt.setDouble(6, loan.getPenaltyAmount());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    loan.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new LibraryException("Ödünç kaydı eklenemedi", e);
        }
    }

    /**
     * READ - Get all loans
     */
    public List<Loan> getAllLoans() throws LibraryException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Loan loan = new Loan(
                    rs.getInt("id"),
                    rs.getInt("student_id"),
                    rs.getInt("book_id"),
                    rs.getDate("loan_date").toLocalDate(),
                    rs.getDate("return_date").toLocalDate(),
                    rs.getDate("actual_return_date") != null ? rs.getDate("actual_return_date").toLocalDate() : null,
                    rs.getDouble("penalty_amount")
                );
                loans.add(loan);
            }
        } catch (SQLException e) {
            throw new LibraryException("Ödünç kayıtları alınamadı", e);
        }
        return loans;
    }

    /**
     * READ - Get active loans (not yet returned)
     */
    public List<Loan> getActiveLoans() throws LibraryException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE actual_return_date IS NULL";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Loan loan = new Loan(
                    rs.getInt("id"),
                    rs.getInt("student_id"),
                    rs.getInt("book_id"),
                    rs.getDate("loan_date").toLocalDate(),
                    rs.getDate("return_date").toLocalDate(),
                    null,
                    rs.getDouble("penalty_amount")
                );
                loans.add(loan);
            }
        } catch (SQLException e) {
            throw new LibraryException("Aktif ödünç kayıtları alınamadı", e);
        }
        return loans;
    }

    /**
     * UPDATE - Update loan information
     */
    public void updateLoan(Loan loan) throws LibraryException {
        String sql = "UPDATE loans SET student_id = ?, book_id = ?, loan_date = ?, return_date = ?, " +
                     "actual_return_date = ?, penalty_amount = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, loan.getStudentId());
            pstmt.setInt(2, loan.getBookId());
            pstmt.setDate(3, Date.valueOf(loan.getLoanDate()));
            pstmt.setDate(4, Date.valueOf(loan.getReturnDate()));
            pstmt.setDate(5, loan.getActualReturnDate() != null ? Date.valueOf(loan.getActualReturnDate()) : null);
            pstmt.setDouble(6, loan.getPenaltyAmount());
            pstmt.setInt(7, loan.getId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new LibraryException("Ödünç kaydı bulunamadı, ID: " + loan.getId());
            }
        } catch (SQLException e) {
            throw new LibraryException("Ödünç kaydı güncellenemedi", e);
        }
    }

    /**
     * DELETE - Delete a loan record
     */
    public void deleteLoan(int id) throws LibraryException {
        String sql = "DELETE FROM loans WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new LibraryException("Ödünç kaydı bulunamadı, ID: " + id);
            }
        } catch (SQLException e) {
            throw new LibraryException("Ödünç kaydı silinemedi", e);
        }
    }

    /**
     * Close database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Veritabanı bağlantısı kapatıldı.");
            }
        } catch (SQLException e) {
            System.err.println("Veritabanı bağlantısı kapatılırken hata: " + e.getMessage());
        }
    }

    /**
     * Get the database connection
     */
    public Connection getConnection() {
        return connection;
    }
}
