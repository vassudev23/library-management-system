package com.bansira.librarymanagement.controller;

import com.bansira.librarymanagement.model.Book;
import com.bansira.librarymanagement.model.Department;
import com.bansira.librarymanagement.service.LeadershipBoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeadershipBoardControllerTest {

    @Mock
    private LeadershipBoardService leadershipBoardService;

    @InjectMocks
    private LeadershipBoardController leadershipBoardController;

    private Book book1;
    private Book book2;
    private Department department;


    @BeforeEach
    public void setup() {
        book1 = Book.builder()
                .title("Book1")
                .author("Author1")
                .ISBN("Isbn1")
                .build();
        book2 = Book.builder()
                .title("Book2")
                .author("Author2")
                .ISBN("Isbn2")
                .build();
        department = Department.builder()
                .name("TestDepartment")
                .build();
    }

    @Test
    public void testAddDepartment() {
        doNothing().when(leadershipBoardService).addDepartment(department);
        ResponseEntity response = leadershipBoardController.addDepartment(department);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(leadershipBoardService, times(1)).addDepartment(department);
    }

    @Test
    public void testGetLastWeekWinner() {
        when(leadershipBoardService.getLastWeekWinner()).thenReturn(department);
        ResponseEntity<Department> response = leadershipBoardController.getLastWeekWinner();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("TestDepartment", response.getBody().getName());
        verify(leadershipBoardService, times(1)).getLastWeekWinner();
    }

    @Test
    public void testGetWeeklyPopularBooks() {
        List<Book> books = Arrays.asList(book1, book2);
        when(leadershipBoardService.getWeeklyPopularBooks()).thenReturn(books);
        ResponseEntity<List<Book>> response = leadershipBoardController.getWeeklyPopularBooks();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(leadershipBoardService, times(1)).getWeeklyPopularBooks();
    }

    @Test
    public void testGetMonthlyPopularBooks() {
        List<Book> books = Arrays.asList(book1, book2);
        when(leadershipBoardService.getMonthlyPopularBooks()).thenReturn(books);
        ResponseEntity<List<Book>> response = leadershipBoardController.getMonthlyPopularBooks();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(leadershipBoardService, times(1)).getMonthlyPopularBooks();
    }

    @Test
    public void testGetTrendingBooks() {
        List<Book> books = Arrays.asList(book1, book2);
        when(leadershipBoardService.getTrendingBooks()).thenReturn(books);
        ResponseEntity<List<Book>> response = leadershipBoardController.getTrendingBooks();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(leadershipBoardService, times(1)).getTrendingBooks();
    }

}

