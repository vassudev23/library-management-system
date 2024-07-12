package com.bansira.librarymanagement.controller;

import com.bansira.librarymanagement.model.Book;
import com.bansira.librarymanagement.service.LibraryManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    @InjectMocks private BookController bookController;

    @Mock private LibraryManagementService libraryManagementService;

    private List<Book> books = new ArrayList<>();
    private Book bookOne;
    private Book bookTwo;

    @BeforeEach
    public void setup(){
        bookOne = Book.builder()
                .ISBN("ISBN1")
                .title("Title1")
                .author("Author1")
                .available(true)
                .build();
        bookTwo = Book.builder()
                .ISBN("ISBN2")
                .title("Title2")
                .author("Author2")
                .available(true)
                .build();
        books.add(bookOne);
        books.add(bookTwo);
    }

    @Test
    public void testListAllBooks() {
        when(libraryManagementService.listAllBooks()).thenReturn(books);
        List<Book> returnedBooks = bookController.listAllBooks();

        assertEquals(2, returnedBooks.size());
        assertEquals("Author1", returnedBooks.get(0).getAuthor());
    }

    @Test
    public void testAddBook() {
        // Create a new book
        Book newBook = Book.builder()
                .genre("Genre")
                .ISBN("ISBN")
                .title("New Book")
                .build();

        // Call controller method to add the book
        when(libraryManagementService.saveBook(newBook)).thenReturn(newBook);
        Book returnedBook = bookController.addBook(newBook);

        assertEquals("New Book", returnedBook.getTitle()); // Check if returned book title matches
    }

    @Test
    public void testRemoveBook() {
        doNothing().when(libraryManagementService).removeBook("ISBN");
        bookController.removeBook("ISBN");
        verify(libraryManagementService, times(1)).removeBook("ISBN");

    }


}
