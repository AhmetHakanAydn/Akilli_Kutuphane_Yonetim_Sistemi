package interfaces;

import model.Book;
import java.util.List;

/**
 * Searchable interface for book search functionality.
 * Demonstrates interface implementation in OOP.
 */
public interface Searchable {
    /**
     * Search books by title
     * @param title Book title to search for
     * @return List of books matching the title
     */
    List<Book> searchByTitle(String title);

    /**
     * Search books by author
     * @param author Author name to search for
     * @return List of books by the author
     */
    List<Book> searchByAuthor(String author);

    /**
     * Search books by category
     * @param category Category to search for
     * @return List of books in the category
     */
    List<Book> searchByCategory(String category);
}
