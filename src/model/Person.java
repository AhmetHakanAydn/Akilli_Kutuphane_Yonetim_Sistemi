package model;

/**
 * Abstract base class representing a person in the library system.
 * Demonstrates inheritance and abstraction in OOP.
 */
public abstract class Person {
    private int id;
    private String name;
    private String email;

    // Constructor overloading - default constructor
    public Person() {
    }

    // Constructor overloading - with name and email
    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Constructor overloading - with all fields
    public Person(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Abstract method to be implemented by subclasses
    public abstract String getRole();

    // Getter and Setter methods (Encapsulation)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Polymorphism - method overriding
    @Override
    public String toString() {
        return "ID: " + id + ", Ad: " + name + ", E-posta: " + email;
    }

    // Polymorphism - method overriding
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
