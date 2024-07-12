package com.bansira.librarymanagement.service;

import com.bansira.librarymanagement.model.Book;

import java.util.List;

public interface LibraryManagementService {

   List<Book> listAllBooks();
   Book getBookByIsbn(String isbn);
   Book saveBook(Book book);
   void removeBook(String isbn);
   List<Book> findBooksByTitle(String title);
   List<Book> findBooksByAuthor(String author);
   List<Book> listAvailableBooks();
   Book downloadBook(String title);
}
