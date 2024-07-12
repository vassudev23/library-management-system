package com.bansira.librarymanagement.service.impl;

import com.bansira.librarymanagement.exception.BadRequestException;
import com.bansira.librarymanagement.model.Book;
import com.bansira.librarymanagement.repository.BooksRepository;
import com.bansira.librarymanagement.service.LeadershipBoardService;
import com.bansira.librarymanagement.service.LibraryManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryManagementServiceImpl implements LibraryManagementService {

    @Autowired private BooksRepository booksRepository;
    @Autowired private LeadershipBoardService leadershipBoardService;


    @Override
    public List<Book> listAllBooks() {
        return booksRepository.findAll();
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        return booksRepository.findByISBN(isbn).orElseThrow(
                () -> new BadRequestException("No Book Found with ISBN : " + isbn));
    }

    @Override
    public Book saveBook(Book book) {
        if(booksRepository.findByISBN(book.getISBN()).isPresent()) {
            throw new BadRequestException("Book already exists with ISBN : " + book.getISBN());
        }
        return booksRepository.save(book);
    }

    @Override
    public void removeBook(String isbn) {
        Book book = booksRepository.findByISBN(isbn).orElseThrow(
                () -> new BadRequestException("No Book Found with ISBN : " + isbn));
        booksRepository.deleteById(book.getId());
    }

    @Override
    public List<Book> findBooksByTitle(String title) {
        return booksRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Book> findBooksByAuthor(String author) {
        return booksRepository.findByAuthorContainingIgnoreCase(author);
    }

    @Override
    public List<Book> listAvailableBooks() {
        return booksRepository.findByAvailableTrue();
    }

    @Override
    public Book downloadBook(String isbn) {
        Book book = getBookByIsbn(isbn);
        if (book != null) {
            book.incrementDownloadCount();
            leadershipBoardService.updateDownloadCount(book.getDepartment());
        }
        return book;
    }
}
