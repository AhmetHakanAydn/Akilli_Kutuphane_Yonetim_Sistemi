package exception;

/**
 * Custom exception class for library operations.
 * Demonstrates exception handling and custom exception creation.
 */
public class LibraryException extends Exception {
    
    // Constructor overloading - with message only
    public LibraryException(String message) {
        super(message);
    }

    // Constructor overloading - with message and cause
    public LibraryException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor overloading - with cause only
    public LibraryException(Throwable cause) {
        super(cause);
    }

    // Constructor overloading - default constructor
    public LibraryException() {
        super("Kütüphane sisteminde bir hata oluştu");
    }
}
