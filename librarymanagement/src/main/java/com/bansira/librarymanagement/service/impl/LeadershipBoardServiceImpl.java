package com.bansira.librarymanagement.service.impl;

import com.bansira.librarymanagement.exception.BadRequestException;
import com.bansira.librarymanagement.model.Book;
import com.bansira.librarymanagement.model.Department;
import com.bansira.librarymanagement.repository.DepartmentsRepository;
import com.bansira.librarymanagement.service.LeadershipBoardService;
import com.bansira.librarymanagement.service.LibraryManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class LeadershipBoardServiceImpl implements LeadershipBoardService {

    @Autowired private DepartmentsRepository departmentsRepository;
    @Autowired private LibraryManagementService libraryManagementService;

    // Method to add a new department
    @Override
    public void addDepartment(Department department) {
        if(departmentsRepository.findById(department.getName()).isPresent()) {
            throw new BadRequestException("Department already exists with name : " + department.getName());
        }
        departmentsRepository.save(department);
    }

    // Method to update department rankings
    @Override
    public List<Department> updateDepartmentRankings() {
        List<Department> departments = departmentsRepository.findAll();
        departments.sort(Comparator.comparingInt(Department::getDownloadCount).reversed());
        return departments.subList(0, Math.min(5, departments.size()));
    }

    // Method to get last week's winner for popular department
    @Override
    public Department getLastWeekWinner() {
        List<Department> departments = departmentsRepository.findAll();
        return departments.stream()
                .max(Comparator.comparingInt(Department::getDownloadCount))
                .orElse(null);
    }

    // Method to update book rankings for weekly popular books
    @Override
    public List<Book> getWeeklyPopularBooks() {
        LocalDateTime lastWeek = LocalDateTime.now().minusWeeks(1);
        List<Book> popularBooks = new ArrayList<>();
        List<Book> books = libraryManagementService.listAllBooks();
        for (Book book : books) {
            if (book.getLastDownloaded().isAfter(lastWeek)) {
                popularBooks.add(book);
            }
        }
        popularBooks.sort(Comparator.comparingInt(Book::getDownloadCount).reversed());
        return popularBooks.subList(0, Math.min(5, popularBooks.size()));
    }

    // Method to update book rankings for monthly popular books
    @Override
    public List<Book> getMonthlyPopularBooks() {
        LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1);
        List<Book> popularBooks = new ArrayList<>();
        List<Book> books = libraryManagementService.listAllBooks();
        for (Book book : books) {
            if (book.getLastDownloaded().isAfter(lastMonth)) {
                popularBooks.add(book);
            }
        }
        popularBooks.sort(Comparator.comparingInt(Book::getDownloadCount).reversed());
        return popularBooks.subList(0, Math.min(5, popularBooks.size()));
    }

    // Method to update book rankings for trending books (updated every hour)
    @Override
    public List<Book> getTrendingBooks() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        List<Book> trendingBooks = new ArrayList<>();
        List<Book> books = libraryManagementService.listAllBooks();
        for (Book book : books) {
            if (book.getLastDownloaded().isAfter(oneHourAgo)) {
                trendingBooks.add(book);
            }
        }
        trendingBooks.sort(Comparator.comparingInt(Book::getDownloadCount).reversed());
        return trendingBooks.subList(0, Math.min(5, trendingBooks.size()));
    }

    @Override
    public void updateDownloadCount(String departmentName) {
        Department department = departmentsRepository.findById(departmentName).orElse(Department.builder().build());
        department.setDownloadCount(department.getDownloadCount() + 1);
        departmentsRepository.save(department);
    }

    @Override
    public void updateLeastDownloadedBook() {
        LocalDateTime twoWeeksAgo = LocalDateTime.now().minusWeeks(2);
        List<Book> books = libraryManagementService.listAllBooks();
        // Checking only for one book, if we want multiple book we can change min to sort
        // and collect the response in list and remove all those books
        Book leastDownloadedBook = books.stream().min(Comparator.comparingInt(Book::getDownloadCount)).get();
        if (leastDownloadedBook.getLastDownloaded().isBefore(twoWeeksAgo)) {
            libraryManagementService.removeBook(leastDownloadedBook.getISBN());
        }
    }
}
