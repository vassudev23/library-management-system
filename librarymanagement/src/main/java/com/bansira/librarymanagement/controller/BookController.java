package com.bansira.librarymanagement.controller;

import com.bansira.librarymanagement.model.Book;
import com.bansira.librarymanagement.service.LibraryManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/books")
public class BookController {

    @Autowired
    private LibraryManagementService libraryManagementService;


    @GetMapping
    public List<Book> listAllBooks() {
        return libraryManagementService.listAllBooks();
    }

    @GetMapping("/{isbn}")
    public Book getBookById(@PathVariable String isbn) {
        return libraryManagementService.getBookByIsbn(isbn);
    }

    @PostMapping
    public Book addBook(@RequestBody Book book) {
        return libraryManagementService.saveBook(book);
    }

    @DeleteMapping("/{isbn}")
    public void removeBook(@PathVariable String isbn) {
        libraryManagementService.removeBook(isbn);
    }

    @GetMapping("/title/{title}")
    public List<Book> findBooksByTitle(@PathVariable String title) {
        return libraryManagementService.findBooksByTitle(title);
    }

    @GetMapping("/author/{author}")
    public List<Book> findBooksByAuthor(@PathVariable String author) {
        return libraryManagementService.findBooksByAuthor(author);
    }

    @GetMapping("/available")
    public List<Book> listAvailableBooks() {
        return libraryManagementService.listAvailableBooks();
    }
}
