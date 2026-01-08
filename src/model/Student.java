package model;

/**
 * Student class that extends Person.
 * Demonstrates inheritance and polymorphism.
 */
public class Student extends Person {
    private String studentNo;

    // Constructor overloading - default constructor
    public Student() {
        super();
    }

    // Constructor overloading - with basic info
    public Student(String name, String email, String studentNo) {
        super(name, email);
        this.studentNo = studentNo;
    }

    // Constructor overloading - with all fields
    public Student(int id, String name, String email, String studentNo) {
        super(id, name, email);
        this.studentNo = studentNo;
    }

    // Implementing abstract method from Person
    @Override
    public String getRole() {
        return "Student";
    }

    // Getter and Setter
    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    // Method overriding - polymorphism
    @Override
    public String toString() {
        return "Student [" + super.toString() + ", Student No: " + studentNo + ", Role: " + getRole() + "]";
    }

    // Method overriding
    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        Student student = (Student) obj;
        return studentNo != null && studentNo.equals(student.studentNo);
    }
}
