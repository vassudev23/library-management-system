package com.bansira.librarymanagement.controller;

import com.bansira.librarymanagement.model.Book;
import com.bansira.librarymanagement.model.Department;
import com.bansira.librarymanagement.service.LeadershipBoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/leadership-board")
@Slf4j
public class LeadershipBoardController {

    @Autowired private LeadershipBoardService leadershipBoardService;


    @PostMapping
    public ResponseEntity addDepartment(@RequestBody Department department) {
        log.info("Request to add new department : {}", department.getName());
        leadershipBoardService.addDepartment(department);
        return ResponseEntity.ok(HttpEntity.EMPTY);
    }


    @GetMapping("/last-week-winner")
    public ResponseEntity<Department> getLastWeekWinner() {
        log.info("Request to get last week winner");
        return ResponseEntity.ok(leadershipBoardService.getLastWeekWinner());
    }

    @GetMapping("/weekly-popular-books")
    public ResponseEntity<List<Book>> getWeeklyPopularBooks() {
        log.info("Request to get weekly popular books");
        return ResponseEntity.ok(leadershipBoardService.getWeeklyPopularBooks());
    }

    @GetMapping("/monthly-popular-books")
    public ResponseEntity<List<Book>> getMonthlyPopularBooks() {
        log.info("Request to get monthly popular books");
        return ResponseEntity.ok(leadershipBoardService.getMonthlyPopularBooks());
    }

    @GetMapping("/trending-books")
    public ResponseEntity<List<Book>> getTrendingBooks() {
        log.info("Request to get trending books");
        return ResponseEntity.ok(leadershipBoardService.getTrendingBooks());
    }

}
