package com.bansira.librarymanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
public class Book {

    @Id
    private Long id;
    private String title;
    private String author;
    private String ISBN;
    private String genre;
    private int publicationYear;
    private String department;
    private boolean available;
    private int downloadCount;
    private LocalDateTime lastDownloaded;


    public void incrementDownloadCount() {
        this.downloadCount++;
        this.lastDownloaded = LocalDateTime.now();
    }
}
