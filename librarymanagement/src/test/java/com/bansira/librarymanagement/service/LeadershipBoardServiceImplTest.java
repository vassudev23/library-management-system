package com.bansira.librarymanagement.service;

import com.bansira.librarymanagement.exception.BadRequestException;
import com.bansira.librarymanagement.model.Book;
import com.bansira.librarymanagement.model.Department;
import com.bansira.librarymanagement.repository.DepartmentsRepository;
import com.bansira.librarymanagement.service.impl.LeadershipBoardServiceImpl;
import com.bansira.librarymanagement.service.impl.LibraryManagementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeadershipBoardServiceImplTest {

    @Mock
    private DepartmentsRepository departmentsRepository;

    @Mock
    private LibraryManagementServiceImpl libraryManagementService;

    @InjectMocks
    private LeadershipBoardServiceImpl leadershipBoardService;

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
    public void testAddDepartment_Success() {
        when(departmentsRepository.findById("TestDepartment")).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> leadershipBoardService.addDepartment(department));
        verify(departmentsRepository, times(1)).save(department);
    }

    @Test
    public void testAddDepartment_AlreadyExists() {
        when(departmentsRepository.findById("TestDepartment")).thenReturn(Optional.of(department));
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            leadershipBoardService.addDepartment(department);
        });
        assertEquals("Department already exists with name : TestDepartment", exception.getMessage());
        verify(departmentsRepository, never()).save(department);
    }

    @Test
    public void testUpdateDepartmentRankings() {
        Department dept1 = Department
                .builder()
                .name("Dept1")
                .build();
        dept1.setDownloadCount(10);
        Department dept2 = Department
                .builder()
                .name("Dept2")
                .build();
        dept2.setDownloadCount(20);
        List<Department> departments = Arrays.asList(dept1, dept2);

        when(departmentsRepository.findAll()).thenReturn(departments);
        List<Department> topDepartments = leadershipBoardService.updateDepartmentRankings();
        assertEquals(2, topDepartments.size());
        assertEquals("Dept2", topDepartments.get(0).getName()); // Assuming descending order by download count
    }

    @Test
    public void testGetLastWeekWinner() {
        Department dept1 = Department
                .builder()
                .name("Dept1")
                .build();
        dept1.setDownloadCount(10);
        Department dept2 = Department
                .builder()
                .name("Dept2")
                .build();
        dept2.setDownloadCount(20);
        List<Department> departments = Arrays.asList(dept1, dept2);
        when(departmentsRepository.findAll()).thenReturn(departments);
        Department lastWeekWinner = leadershipBoardService.getLastWeekWinner();
        assertEquals("Dept2", lastWeekWinner.getName()); // Assuming max by download count
    }

    @Test
    public void testGetWeeklyPopularBooks() {
        LocalDateTime lastWeek = LocalDateTime.now().minusWeeks(1);
        book1.setDownloadCount(10);
        book1.setLastDownloaded(lastWeek.plusDays(1)); // Within last week
        book2.setDownloadCount(20);
        book2.setLastDownloaded(lastWeek.minusDays(1)); // Outside last week
        List<Book> books = Arrays.asList(book1, book2);
        when(libraryManagementService.listAllBooks()).thenReturn(books);
        List<Book> weeklyPopularBooks = leadershipBoardService.getWeeklyPopularBooks();
        assertEquals(1, weeklyPopularBooks.size());
        assertEquals("Book1", weeklyPopularBooks.get(0).getTitle()); // Assuming descending order by download count
    }

    @Test
    public void testGetMonthlyPopularBooks() {
        LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1);
        book1.setDownloadCount(10);
        book1.setLastDownloaded(lastMonth.plusDays(1)); // Within last month
        book2.setDownloadCount(20);
        book2.setLastDownloaded(lastMonth.minusDays(1)); // Outside last month
        List<Book> books = Arrays.asList(book1, book2);
        when(libraryManagementService.listAllBooks()).thenReturn(books);
        List<Book> monthlyPopularBooks = leadershipBoardService.getMonthlyPopularBooks();
        assertEquals(1, monthlyPopularBooks.size());
        assertEquals("Book1", monthlyPopularBooks.get(0).getTitle()); // Assuming descending order by download count
    }

    @Test
    public void testGetTrendingBooks() {
        // Prepare
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        book1.setDownloadCount(10);
        book1.setLastDownloaded(oneHourAgo.plus(30, ChronoUnit.MINUTES)); // Within last hour
        book2.setDownloadCount(20);
        book2.setLastDownloaded(oneHourAgo.minus(30, ChronoUnit.MINUTES)); // Outside last hour
        List<Book> books = Arrays.asList(book1, book2);
        when(libraryManagementService.listAllBooks()).thenReturn(books);
        List<Book> trendingBooks = leadershipBoardService.getTrendingBooks();
        assertEquals(1, trendingBooks.size());
        assertEquals("Book1", trendingBooks.get(0).getTitle()); // Assuming descending order by download count
    }

    @Test
    public void testUpdateDownloadCount() {
        String departmentName = "TestDepartment";
        department.setDownloadCount(5);
        when(departmentsRepository.findById(departmentName)).thenReturn(Optional.of(department));
        when(departmentsRepository.save(any(Department.class))).thenReturn(department);
        leadershipBoardService.updateDownloadCount(departmentName);
        assertEquals(6, department.getDownloadCount()); // Check if download count incremented
        verify(departmentsRepository, times(1)).save(department);
    }

    @Test
    public void testUpdateLeastDownloadedBook() {
        LocalDateTime twoWeeksAgo = LocalDateTime.now().minusWeeks(2);
        Book leastDownloadedBook = Book.builder()
                .title("Book1")
                .author("Author1")
                .build();
        leastDownloadedBook.setISBN("123456");
        leastDownloadedBook.setDownloadCount(5);
        leastDownloadedBook.setLastDownloaded(twoWeeksAgo.minusDays(1));
        Book popularBook = Book.builder()
                .title("Book2")
                .author("Author2")
                .build();
        popularBook.setISBN("789012");
        popularBook.setDownloadCount(20);
        popularBook.setLastDownloaded(LocalDateTime.now());
        List<Book> books = Arrays.asList(leastDownloadedBook, popularBook);
        when(libraryManagementService.listAllBooks()).thenReturn(books);
        doNothing().when(libraryManagementService).removeBook("123456");
        leadershipBoardService.updateLeastDownloadedBook();
        verify(libraryManagementService, times(1)).removeBook("123456");
        verify(libraryManagementService, never()).removeBook("789012"); // Popular book should not be removed
    }


}

