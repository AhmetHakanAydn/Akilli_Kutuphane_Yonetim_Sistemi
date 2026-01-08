-- Akıllı Kütüphane Yönetim Sistemi - Database Setup Script
-- This script creates the necessary tables for the library management system

-- Create students table
CREATE TABLE IF NOT EXISTS students (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(100) NOT NULL,
    student_no VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(100)
);

-- Create books table
CREATE TABLE IF NOT EXISTS books (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    is_available BOOLEAN DEFAULT TRUE
);

-- Create loans table
CREATE TABLE IF NOT EXISTS loans (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    loan_date DATE NOT NULL,
    return_date DATE NOT NULL,
    actual_return_date DATE,
    penalty_amount DECIMAL(10,2) DEFAULT 0,
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);

-- Sample data for testing (optional)
-- Uncomment the following lines to insert sample data

-- Insert sample students
-- INSERT INTO students (name, student_no, email) VALUES ('Ali Yılmaz', '2021001', 'ali.yilmaz@university.edu');
-- INSERT INTO students (name, student_no, email) VALUES ('Ayşe Demir', '2021002', 'ayse.demir@university.edu');
-- INSERT INTO students (name, student_no, email) VALUES ('Mehmet Kaya', '2021003', 'mehmet.kaya@university.edu');

-- Insert sample books
-- INSERT INTO books (title, author, category, is_available) VALUES ('Java Programming', 'John Doe', 'Programming', TRUE);
-- INSERT INTO books (title, author, category, is_available) VALUES ('Database Design', 'Jane Smith', 'Database', TRUE);
-- INSERT INTO books (title, author, category, is_available) VALUES ('Artificial Intelligence', 'Bob Johnson', 'AI', TRUE);
-- INSERT INTO books (title, author, category, is_available) VALUES ('Web Development', 'Alice Brown', 'Programming', TRUE);
-- INSERT INTO books (title, author, category, is_available) VALUES ('Data Structures', 'Charlie Wilson', 'Programming', TRUE);
