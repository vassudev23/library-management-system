package com.bansira.librarymanagement.service;

import com.bansira.librarymanagement.exception.BadRequestException;
import com.bansira.librarymanagement.model.Book;
import com.bansira.librarymanagement.repository.BooksRepository;
import com.bansira.librarymanagement.service.impl.LibraryManagementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LibraryManagementServiceImplTest {

    @Mock
    private BooksRepository booksRepository;

    @InjectMocks
    private LibraryManagementServiceImpl libraryManagementService;

    private Book book1;
    private Book book2;

    @BeforeEach
    public void setup() {
        book1 = Book.builder()
                .ISBN("ISBN1")
                .author("Author1")
                .available(true)
                .title("Book1")
                .build();
        book2 = Book.builder()
                .ISBN("ISBN2")
                .author("Author2")
                .available(true)
                .title("Book2")
                .build();
    }

    @Test
    public void testListAllBooks() {
        // Mock data
        when(booksRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        // Call service method
        List<Book> returnedBooks = libraryManagementService.listAllBooks();

        // Assert
        assertEquals(2, returnedBooks.size());
        verify(booksRepository, times(1)).findAll();
    }

    @Test
    public void testGetBookByIsbn() {
        // Mock data
        String isbn = "ISBN1";
        when(booksRepository.findByISBN(isbn)).thenReturn(Optional.of(book1));

        // Call service method
        Book returnedBook = libraryManagementService.getBookByIsbn(isbn);

        // Assert
        assertEquals("Book1", returnedBook.getTitle());
    }

    @Test
    public void testGetBookByIsbn_NotFound() {
        // Mock data
        String isbn = "ISBN999";
        when(booksRepository.findByISBN(isbn)).thenReturn(Optional.empty());

        // Call service method and assert for exception
        assertThrows(BadRequestException.class, () -> libraryManagementService.getBookByIsbn(isbn));
    }

    @Test
    public void testSaveBook() {
        // Mock data
        when(booksRepository.findByISBN(book1.getISBN())).thenReturn(Optional.empty());
        when(booksRepository.save(any(Book.class))).thenReturn(book1);

        // Call service method
        Book returnedBook = libraryManagementService.saveBook(book1);

        // Assert
        assertEquals("Book1", returnedBook.getTitle());
    }

    @Test
    public void testSaveBook_DuplicateISBN() {
        // Mock data
        when(booksRepository.findByISBN(book1.getISBN())).thenReturn(Optional.of(book1));

        // Call service method and assert for exception
        assertThrows(BadRequestException.class, () -> libraryManagementService.saveBook(book1));
    }

    @Test
    public void testRemoveBook() {
        // Mock data
        String isbn = "ISBN1";
        when(booksRepository.findByISBN(isbn)).thenReturn(Optional.of(book1));

        // Call service method
        libraryManagementService.removeBook(isbn);

        // Verify that repository method was called once
        verify(booksRepository, times(1)).deleteById(book1.getId());
    }

    @Test
    public void testRemoveBook_NotFound() {
        // Mock data
        String isbn = "ISBN999";
        when(booksRepository.findByISBN(isbn)).thenReturn(Optional.empty());

        // Call service method and assert for exception
        assertThrows(BadRequestException.class, () -> libraryManagementService.removeBook(isbn));
    }

    @Test
    public void testFindBooksByTitle() {
        // Mock data
        String title = "Book1";
        when(booksRepository.findByTitleContainingIgnoreCase(title)).thenReturn(Arrays.asList(book1));

        // Call service method
        List<Book> returnedBooks = libraryManagementService.findBooksByTitle(title);

        // Assert
        assertEquals(1, returnedBooks.size());
        assertEquals("Book1", returnedBooks.get(0).getTitle());
    }

    @Test
    public void testFindBooksByAuthor() {
        // Mock data
        String author = "Author1";
        when(booksRepository.findByAuthorContainingIgnoreCase(author)).thenReturn(Arrays.asList(book1));

        // Call service method
        List<Book> returnedBooks = libraryManagementService.findBooksByAuthor(author);

        // Assert
        assertEquals(1, returnedBooks.size());
        assertEquals("Book1", returnedBooks.get(0).getTitle());
    }

    @Test
    public void testListAvailableBooks() {
        // Mock data
        when(booksRepository.findByAvailableTrue()).thenReturn(Arrays.asList(book1, book2));

        // Call service method
        List<Book> returnedBooks = libraryManagementService.listAvailableBooks();

        // Assert
        assertEquals(2, returnedBooks.size());
        assertEquals("Book1", returnedBooks.get(0).getTitle());
        assertEquals("Book2", returnedBooks.get(1).getTitle());
    }
}