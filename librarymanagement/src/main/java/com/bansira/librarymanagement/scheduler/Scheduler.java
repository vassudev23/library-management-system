package com.bansira.librarymanagement.scheduler;

import com.bansira.librarymanagement.service.LeadershipBoardService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Scheduler {

    @Autowired private LeadershipBoardService leadershipBoardService;

    // Scheduler to remove book if it remains in least read/downloaded for consecutive 2 weeks
    // Scheduler will run every hour
    public void updateLeastDownloadedBooks() {
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(
                        () -> leadershipBoardService.updateLeastDownloadedBook(),
                        0,
                        1,
                        TimeUnit.HOURS);
    }
}
