# Akıllı Kütüphane Yönetim Sistemi
## Smart Library Management System

A comprehensive Java-based library management system for university libraries, featuring book management, student registration, and loan tracking with penalty calculation.

## Features

### OOP Implementation
- ✅ **5+ Classes**: Person, Student, Book, Loan, LibraryManager, DatabaseManager, LibraryException
- ✅ **Abstract Class**: `Person` (abstract base class)
- ✅ **Interface**: `Searchable` (implemented by LibraryManager)
- ✅ **Inheritance**: `Student extends Person`
- ✅ **Polymorphism**: Method overriding (toString, equals)
- ✅ **Encapsulation**: All fields private with getters/setters
- ✅ **Constructor Overloading**: Multiple constructors per class
- ✅ **Method Overloading**: Multiple methods with same name

### Core Functionality
- **Book Operations**: Add, list, search, update, delete books
- **Student Operations**: Add, list, update, delete students
- **Loan Operations**: Borrow books, return books, track active/overdue loans
- **Search System**: Search books by title, author, or category
- **Penalty Calculation**: Automatic penalty calculation (5 TL per day)
- **Availability Tracking**: Real-time book availability status

### Technical Features
- **Database**: SQLite with JDBC
- **Collections**: ArrayList<Book>, ArrayList<Student>, ArrayList<Loan>
- **Exception Handling**: Custom LibraryException with try-catch blocks
- **CRUD Operations**: Full Create, Read, Update, Delete support
- **Console UI**: User-friendly menu system with switch-case logic

## Project Structure

```
src/
├── Main.java                          # Main application with menu system
├── model/
│   ├── Person.java                    # Abstract base class
│   ├── Student.java                   # Student class (extends Person)
│   ├── Book.java                      # Book entity
│   └── Loan.java                      # Loan transaction with penalty logic
├── manager/
│   ├── LibraryManager.java           # Business logic (implements Searchable)
│   └── DatabaseManager.java          # JDBC database operations
├── interfaces/
│   └── Searchable.java               # Search interface
└── exception/
    └── LibraryException.java         # Custom exception class

database_setup.sql                     # Database schema
library.db                            # SQLite database (auto-created)
```

## Prerequisites

- Java 8 or higher
- SQLite JDBC Driver (included in most Java distributions or download separately)

## Installation & Setup

### 1. Download SQLite JDBC Driver

Download the SQLite JDBC driver from: https://github.com/xerial/sqlite-jdbc/releases

Or if using Maven, add to pom.xml:
```xml
<dependency>
    <groupId>org.xerial</groupId>
    <artifactId>sqlite-jdbc</artifactId>
    <version>3.45.0.0</version>
</dependency>
```

### 2. Compile the Project

```bash
# Navigate to project directory
cd Akilli_Kutuphane_Yonetim_Sistemi

# Compile all Java files (if SQLite JDBC is in current directory)
javac -cp ".:sqlite-jdbc-3.45.0.0.jar" src/**/*.java src/*.java

# Or compile without external JAR if using system SQLite driver
javac src/**/*.java src/*.java
```

### 3. Run the Application

```bash
# Run with SQLite JDBC in classpath
java -cp ".:sqlite-jdbc-3.45.0.0.jar:src" Main

# Or run without external JAR
java -cp src Main
```

The database will be automatically created on first run as `library.db`.

## Usage

### Main Menu
1. **Book Operations**
   - Add Book
   - List All Books
   - Search Books (by title, author, category)
   - Update Book
   - Delete Book

2. **Student Operations**
   - Add Student
   - List All Students
   - Update Student
   - Delete Student

3. **Loan Operations**
   - Borrow Book (with loan duration)
   - Return Book (with automatic penalty calculation)
   - List Active Loans
   - List Overdue Loans (with current penalties)

### Example Workflow

1. **Add Students**: Register students with name, student number, and email
2. **Add Books**: Add books with title, author, and category
3. **Borrow Books**: Students can borrow available books with specified loan duration
4. **Return Books**: Return books and automatically calculate penalties for late returns
5. **Track Overdue**: Monitor overdue loans and associated penalties (5 TL/day)

## Database Schema

### students
- `id`: Primary key (auto-increment)
- `name`: Student name
- `student_no`: Unique student number
- `email`: Student email

### books
- `id`: Primary key (auto-increment)
- `title`: Book title
- `author`: Author name
- `category`: Book category
- `is_available`: Availability status

### loans
- `id`: Primary key (auto-increment)
- `student_id`: Foreign key to students
- `book_id`: Foreign key to books
- `loan_date`: Date book was borrowed
- `return_date`: Expected return date
- `actual_return_date`: Actual return date (null if not returned)
- `penalty_amount`: Late fee (5 TL per day)

## OOP Concepts Demonstrated

### 1. Abstraction
- `Person` abstract class with abstract method `getRole()`

### 2. Inheritance
- `Student extends Person`

### 3. Polymorphism
- Method overriding: `toString()`, `equals()`, `getRole()`
- Method overloading: Multiple constructors and methods

### 4. Encapsulation
- All class fields are private
- Public getter/setter methods

### 5. Interface Implementation
- `LibraryManager implements Searchable`
- Search methods: `searchByTitle()`, `searchByAuthor()`, `searchByCategory()`

### 6. Exception Handling
- Custom `LibraryException` class
- Try-catch blocks throughout
- Proper error messages and handling

### 7. Collections & Generics
- `ArrayList<Book>`, `ArrayList<Student>`, `ArrayList<Loan>`
- Type-safe generic collections

## Business Rules

1. **Book Availability**: Books can only be borrowed if available
2. **Unique Student Numbers**: Each student must have unique student number
3. **Penalty Calculation**: 5 TL penalty per day for late returns
4. **Active Loan Check**: Cannot delete books or students with active loans
5. **SQL Injection Prevention**: Uses PreparedStatement for all queries

## Error Handling

The system handles various error scenarios:
- Database connection failures
- Invalid input (non-numeric where numeric expected)
- Duplicate student numbers
- Borrowing unavailable books
- Deleting resources with dependencies
- Book/Student not found errors

## Security Features

- **SQL Injection Prevention**: All queries use PreparedStatement
- **Input Validation**: Validates all user inputs
- **Data Integrity**: Foreign key constraints in database
- **Transaction Safety**: Proper connection management

## Future Enhancements

Possible improvements:
- GUI using JavaFX or Swing
- Book reservation system
- Multiple copies of same book
- User authentication and roles
- Email notifications for overdue books
- Report generation (PDF/CSV)
- Advanced search with multiple filters
- Book recommendations

## License

This project is created for educational purposes as part of a university assignment.

## Author

Ahmet Hakan Aydın

## Contact

For questions or issues, please create an issue in the repository.