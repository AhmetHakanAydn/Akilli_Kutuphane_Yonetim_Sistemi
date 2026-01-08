package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Loan class representing a book loan transaction.
 * Demonstrates encapsulation and business logic.
 */
public class Loan {
    private int id;
    private int studentId;
    private int bookId;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private LocalDate actualReturnDate;
    private double penaltyAmount;
    
    // Daily penalty rate (5 TL per day)
    private static final double DAILY_PENALTY_RATE = 5.0;

    // Constructor overloading - default constructor
    public Loan() {
    }

    // Constructor overloading - with basic info
    public Loan(int studentId, int bookId, LocalDate loanDate, LocalDate returnDate) {
        this.studentId = studentId;
        this.bookId = bookId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.penaltyAmount = 0.0;
    }

    // Constructor overloading - with all fields
    public Loan(int id, int studentId, int bookId, LocalDate loanDate, 
                LocalDate returnDate, LocalDate actualReturnDate, double penaltyAmount) {
        this.id = id;
        this.studentId = studentId;
        this.bookId = bookId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.actualReturnDate = actualReturnDate;
        this.penaltyAmount = penaltyAmount;
    }

    /**
     * Calculate penalty based on the difference between return date and actual return date.
     * 5 TL penalty for each day of delay.
     */
    public void calculatePenalty() {
        if (actualReturnDate != null && actualReturnDate.isAfter(returnDate)) {
            long daysLate = ChronoUnit.DAYS.between(returnDate, actualReturnDate);
            this.penaltyAmount = daysLate * DAILY_PENALTY_RATE;
        } else {
            this.penaltyAmount = 0.0;
        }
    }

    // Method overloading - calculate penalty with custom return date
    public double calculatePenalty(LocalDate customActualReturnDate) {
        if (customActualReturnDate != null && customActualReturnDate.isAfter(returnDate)) {
            long daysLate = ChronoUnit.DAYS.between(returnDate, customActualReturnDate);
            return daysLate * DAILY_PENALTY_RATE;
        }
        return 0.0;
    }

    // Getter and Setter methods (Encapsulation)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public LocalDate getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(LocalDate actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
        calculatePenalty();
    }

    public double getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(double penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    @Override
    public String toString() {
        return "Loan [ID: " + id + ", Student ID: " + studentId + ", Book ID: " + bookId +
               ", Loan Date: " + loanDate + ", Return Date: " + returnDate +
               ", Actual Return: " + (actualReturnDate != null ? actualReturnDate : "Not returned") +
               ", Penalty: " + penaltyAmount + " TL]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Loan loan = (Loan) obj;
        return id == loan.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
