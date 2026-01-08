# Proje Raporu: Akıllı Kütüphane Yönetim Sistemi

## Proje Özeti

Bu proje, üniversite kütüphanelerinde kullanılmak üzere geliştirilmiş kapsamlı bir kütüphane yönetim sistemidir. Java programlama dili kullanılarak geliştirilmiştir ve nesne tabanlı programlama (OOP) kavramlarının tümünü içermektedir.

## Kullanılan OOP Kavramları

### 1. Soyutlama (Abstraction)
- **Person**: Abstract sınıf olarak tanımlanmıştır
- `getRole()` abstract metodu alt sınıflar tarafından implement edilir
- Ortak özellikleri (id, name, email) ve metotları içerir

```java
public abstract class Person {
    private int id;
    private String name;
    private String email;
    
    public abstract String getRole();
}
```

### 2. Kalıtım (Inheritance)
- **Student** sınıfı **Person** sınıfından türetilmiştir
- Student, Person'dan tüm özellikleri ve metotları miras alır
- Ek olarak studentNo özelliğini ekler

```java
public class Student extends Person {
    private String studentNo;
    
    @Override
    public String getRole() {
        return "Student";
    }
}
```

### 3. Çok Biçimlilik (Polymorphism)
- **Method Overriding**: toString(), equals(), hashCode() metotları override edilmiştir
- **Method Overloading**: 
  - Loan sınıfında `calculatePenalty()` metodu overload edilmiştir
  - Her sınıfta constructor overloading kullanılmıştır

```java
// Method Overriding
@Override
public String toString() {
    return "Student [" + super.toString() + "...]";
}

// Method Overloading
public void calculatePenalty() { ... }
public double calculatePenalty(LocalDate customDate) { ... }
```

### 4. Kapsülleme (Encapsulation)
- Tüm sınıflarda alanlar **private** olarak tanımlanmıştır
- Public getter ve setter metotları ile erişim sağlanır
- Veri gizliliği ve güvenliği sağlanır

```java
private String title;

public String getTitle() {
    return title;
}

public void setTitle(String title) {
    this.title = title;
}
```

### 5. Interface (Arayüz)
- **Searchable** interface tanımlanmıştır
- LibraryManager tarafından implement edilir
- Üç arama metodu tanımlar: searchByTitle, searchByAuthor, searchByCategory

```java
public interface Searchable {
    List<Book> searchByTitle(String title);
    List<Book> searchByAuthor(String author);
    List<Book> searchByCategory(String category);
}

public class LibraryManager implements Searchable {
    @Override
    public List<Book> searchByTitle(String title) { ... }
}
```

### 6. Constructor Overloading
Her sınıfta farklı parametreli constructor'lar:

```java
public Book() { ... }
public Book(String title, String author, String category) { ... }
public Book(int id, String title, String author, String category, boolean isAvailable) { ... }
```

## Sınıf Diyagramı

```
Person (Abstract)
   ↑
   |
Student

Searchable (Interface)
   ↑
   |
LibraryManager

Independent Classes:
- Book
- Loan
- DatabaseManager
- LibraryException
```

## Veritabanı Şeması

### students
- id (PK)
- name
- student_no (UNIQUE)
- email

### books
- id (PK)
- title
- author
- category
- is_available

### loans
- id (PK)
- student_id (FK → students.id)
- book_id (FK → books.id)
- loan_date
- return_date
- actual_return_date
- penalty_amount

## CRUD İşlemleri

Projede her varlık için tam CRUD işlemleri implement edilmiştir:

### Book CRUD
- **CREATE**: `addBook()` - Yeni kitap ekleme
- **READ**: `getAllBooks()`, `getBookById()` - Kitap listeleme/okuma
- **UPDATE**: `updateBook()` - Kitap güncelleme
- **DELETE**: `deleteBook()` - Kitap silme

### Student CRUD
- **CREATE**: `addStudent()`
- **READ**: `getAllStudents()`, `getStudentById()`
- **UPDATE**: `updateStudent()`
- **DELETE**: `deleteStudent()`

### Loan CRUD
- **CREATE**: `addLoan()`
- **READ**: `getAllLoans()`, `getActiveLoans()`
- **UPDATE**: `updateLoan()`
- **DELETE**: `deleteLoan()`

## Hata Yönetimi

### LibraryException
Özel exception sınıfı ile sistemdeki hatalar yönetilir:

```java
public class LibraryException extends Exception {
    public LibraryException(String message) { ... }
    public LibraryException(String message, Throwable cause) { ... }
}
```

### Try-Catch Kullanımı
- Database işlemlerinde SQL hatalarını yakalama
- PreparedStatement kullanarak SQL Injection önleme
- Kullanıcı input hatalarını yakalama
- Connection kapatma işlemlerinde finally bloğu

## Koleksiyonlar ve Generic Yapılar

```java
private List<Book> books = new ArrayList<>();
private List<Student> students = new ArrayList<>();
private List<Loan> loans = new ArrayList<>();
```

- ArrayList kullanımı ile dinamik listeler
- Generic yapılar ile tip güvenliği
- Stream API kullanımı (filter, collect)

## İş Kuralları ve Mantığı

### Ceza Hesaplama
- Her gün gecikme için 5 TL ceza
- `ChronoUnit.DAYS.between()` ile gün farkı hesaplama
- Otomatik ceza hesaplama sistemi

### Kitap Müsaitlik Kontrolü
- Ödünç verildiğinde `is_available = false`
- Teslim edildiğinde `is_available = true`
- Müsait olmayan kitap tekrar verilemez

### Veri Bütünlüğü
- Foreign key constraints ile ilişkisel bütünlük
- Aktif ödünç varsa öğrenci/kitap silinemez
- UNIQUE constraint ile öğrenci numarası tekil

## Özellikler

### Kullanıcı Arayüzü
- Console tabanlı menü sistemi
- Switch-case ile menü kontrolü
- While döngüleri ile sürekli çalışma
- Anlaşılır hata mesajları

### JDBC Kullanımı
- SQLite database
- PreparedStatement ile güvenli sorgular
- Connection management
- Auto-generated keys

### Arama Sistemi
- Başlığa göre arama
- Yazara göre arama
- Kategoriye göre arama
- Case-insensitive arama

## Kod Kalitesi

### Best Practices
- Single Responsibility Principle
- Encapsulation
- DRY (Don't Repeat Yourself)
- Anlamlı değişken ve metot isimleri
- Yorum satırları ile açıklamalar

### Güvenlik
- SQL Injection prevention (PreparedStatement)
- Input validation
- Exception handling
- Connection closing (finally bloğu)

## Sonuç

Bu proje, nesne tabanlı programlamanın tüm temel kavramlarını kapsamlı bir şekilde göstermektedir:
- ✅ 5+ sınıf
- ✅ Abstract sınıf
- ✅ Interface
- ✅ Kalıtım
- ✅ Çok biçimlilik
- ✅ Kapsülleme
- ✅ JDBC ve veritabanı
- ✅ CRUD işlemleri
- ✅ Exception handling
- ✅ Koleksiyonlar ve generic yapılar

Sistem, gerçek dünya kullanımına uygun, güvenli ve sürdürülebilir bir yapıya sahiptir.

---

**Geliştirici**: Ahmet Hakan Aydın
**Tarih**: 2026
**Programlama Dili**: Java
**Veritabanı**: SQLite
