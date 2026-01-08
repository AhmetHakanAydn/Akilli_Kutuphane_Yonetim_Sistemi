package model;

/**
 * Book class representing a book in the library.
 * Demonstrates encapsulation and constructor overloading.
 */
public class Book {
    private int id;
    private String title;
    private String author;
    private String category;
    private boolean isAvailable;

    // Constructor overloading - default constructor
    public Book() {
        this.isAvailable = true;
    }

    // Constructor overloading - with basic info
    public Book(String title, String author, String category) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.isAvailable = true;
    }

    // Constructor overloading - with all fields
    public Book(int id, String title, String author, String category, boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isAvailable = isAvailable;
    }

    // Getter and Setter methods (Encapsulation)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    // Method overriding - polymorphism
    @Override
    public String toString() {
        return "Book [ID: " + id + ", Title: " + title + ", Author: " + author + 
               ", Category: " + category + ", Available: " + (isAvailable ? "Yes" : "No") + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return id == book.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
