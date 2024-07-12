package com.bansira.librarymanagement.service;

import com.bansira.librarymanagement.model.Book;
import com.bansira.librarymanagement.model.Department;

import java.util.List;

public interface LeadershipBoardService {

    void addDepartment(Department department);
    List<Department> updateDepartmentRankings();
    Department getLastWeekWinner();
    List<Book> getWeeklyPopularBooks();
    List<Book> getMonthlyPopularBooks();
    List<Book> getTrendingBooks();
    void updateDownloadCount(String departmentName);
    void updateLeastDownloadedBook();
}
