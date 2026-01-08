# Class Diagram - Akıllı Kütüphane Yönetim Sistemi

## Class Relationships

```
┌─────────────────────────────────────────────────────────────────┐
│                       CLASS HIERARCHY                            │
└─────────────────────────────────────────────────────────────────┘

┌──────────────────┐
│     Person       │ (Abstract Class)
│  <<abstract>>    │
├──────────────────┤
│ - id: int        │
│ - name: String   │
│ - email: String  │
├──────────────────┤
│ + abstract       │
│   getRole()      │
└────────┬─────────┘
         │
         │ extends
         ▼
┌──────────────────┐
│     Student      │
├──────────────────┤
│ - studentNo:     │
│   String         │
├──────────────────┤
│ + getRole():     │
│   String         │
└──────────────────┘


┌──────────────────┐
│   Searchable     │ (Interface)
│  <<interface>>   │
├──────────────────┤
│ + searchByTitle  │
│ + searchByAuthor │
│ + searchBy       │
│   Category       │
└────────┬─────────┘
         │
         │ implements
         ▼
┌──────────────────────────────────────┐
│         LibraryManager               │
├──────────────────────────────────────┤
│ - books: ArrayList<Book>             │
│ - students: ArrayList<Student>       │
│ - loans: ArrayList<Loan>             │
│ - databaseManager: DatabaseManager   │
├──────────────────────────────────────┤
│ + addBook(Book)                      │
│ + updateBook(Book)                   │
│ + deleteBook(int)                    │
│ + addStudent(Student)                │
│ + updateStudent(Student)             │
│ + deleteStudent(int)                 │
│ + borrowBook(int, int, int)          │
│ + returnBook(int)                    │
│ + listActiveLoans()                  │
│ + listOverdueLoans()                 │
│ + searchByTitle(String): List<Book>  │
│ + searchByAuthor(String): List<Book> │
│ + searchByCategory(String): List<Book>│
└──────────┬───────────────────────────┘
           │
           │ uses
           ▼
┌──────────────────────────────────────┐
│       DatabaseManager                │
├──────────────────────────────────────┤
│ - connection: Connection             │
├──────────────────────────────────────┤
│ CRUD Operations:                     │
│ + addBook(Book)                      │
│ + getAllBooks(): List<Book>          │
│ + getBookById(int): Book             │
│ + updateBook(Book)                   │
│ + deleteBook(int)                    │
│ + addStudent(Student)                │
│ + getAllStudents(): List<Student>    │
│ + getStudentById(int): Student       │
│ + updateStudent(Student)             │
│ + deleteStudent(int)                 │
│ + addLoan(Loan)                      │
│ + getAllLoans(): List<Loan>          │
│ + getActiveLoans(): List<Loan>       │
│ + updateLoan(Loan)                   │
│ + deleteLoan(int)                    │
│ + closeConnection()                  │
└──────────────────────────────────────┘


┌──────────────────┐    ┌──────────────────┐    ┌──────────────────┐
│      Book        │    │      Loan        │    │ LibraryException │
├──────────────────┤    ├──────────────────┤    ├──────────────────┤
│ - id: int        │    │ - id: int        │    │ (Extends         │
│ - title: String  │    │ - studentId: int │    │  Exception)      │
│ - author: String │    │ - bookId: int    │    ├──────────────────┤
│ - category:      │    │ - loanDate:      │    │ + LibraryException
│   String         │    │   LocalDate      │    │   (String)       │
│ - isAvailable:   │    │ - returnDate:    │    │ + LibraryException
│   boolean        │    │   LocalDate      │    │   (String,       │
├──────────────────┤    │ - actualReturn   │    │    Throwable)    │
│ + constructors   │    │   Date: LocalDate│    └──────────────────┘
│   (3 versions)   │    │ - penaltyAmount: │
│ + getters/setters│    │   double         │
│ + toString()     │    ├──────────────────┤
│ + equals()       │    │ + calculatePenalty()
│ + hashCode()     │    │ + calculatePenalty
└──────────────────┘    │   (LocalDate)    │
                        │ + getters/setters│
                        │ + toString()     │
                        │ + equals()       │
                        │ + hashCode()     │
                        └──────────────────┘


┌──────────────────────────────────────┐
│              Main                    │
├──────────────────────────────────────┤
│ - libraryManager: LibraryManager     │
│ - databaseManager: DatabaseManager   │
│ - scanner: Scanner                   │
├──────────────────────────────────────┤
│ + main(String[])                     │
│ - displayMainMenu()                  │
│ - bookOperationsMenu()               │
│ - studentOperationsMenu()            │
│ - loanOperationsMenu()               │
│ - addBook()                          │
│ - searchBooks()                      │
│ - updateBook()                       │
│ - deleteBook()                       │
│ - addStudent()                       │
│ - updateStudent()                    │
│ - deleteStudent()                    │
│ - borrowBook()                       │
│ - returnBook()                       │
│ - getIntInput(String): int           │
└──────────────────────────────────────┘
```

## Relationships Summary

### Inheritance
- `Student` **extends** `Person` (abstract class)
  - Student inherits id, name, email from Person
  - Student implements abstract method getRole()

### Interface Implementation
- `LibraryManager` **implements** `Searchable`
  - Implements searchByTitle()
  - Implements searchByAuthor()
  - Implements searchByCategory()

### Composition
- `LibraryManager` **has-a** `DatabaseManager`
- `LibraryManager` **has-many** `Book` objects (ArrayList)
- `LibraryManager` **has-many** `Student` objects (ArrayList)
- `LibraryManager` **has-many** `Loan` objects (ArrayList)

### Association
- `Loan` associates `Student` and `Book` (via IDs)
- `Main` uses `LibraryManager` and `DatabaseManager`

### Exception Hierarchy
- `LibraryException` **extends** `Exception`

## OOP Principles Demonstrated

### 1. Abstraction
- Person is abstract, hiding implementation details
- Abstract method getRole() must be implemented by subclasses

### 2. Inheritance
- Student inherits all properties and methods from Person
- Code reuse through inheritance

### 3. Polymorphism
- Method overriding: toString(), equals(), getRole()
- Method overloading: constructors, calculatePenalty()

### 4. Encapsulation
- All fields private
- Access through public getters/setters
- Data hiding and protection

### 5. Interface
- Searchable defines contract for search operations
- LibraryManager provides implementation

## Method Interactions

```
Main.borrowBook()
    ↓
LibraryManager.borrowBook()
    ↓
├─→ LibraryManager.getStudentById()
│       ↓
│   DatabaseManager.getStudentById()
│
├─→ LibraryManager.getBookById()
│       ↓
│   DatabaseManager.getBookById()
│
├─→ DatabaseManager.addLoan()
│
└─→ DatabaseManager.updateBook()
```

## Database Relationships

```
students (1) ──── (N) loans (N) ──── (1) books
   ↑                                      ↑
   │                                      │
   └────── Foreign Keys: ─────────────────┘
           student_id, book_id
```

This diagram shows all class relationships, inheritance hierarchy, interface implementation, and method interactions in the system.
