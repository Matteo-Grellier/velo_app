package com.formation.velo.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.formation.velo.service.StationsService;

import lombok.extern.java.Log;

@Log
@Component
public class ScheduledTask {
    private final StationsService stationsService;

    public ScheduledTask(StationsService stationsService) {
        this.stationsService = stationsService;
    }

    @Scheduled(fixedRate = 60000)
    public void searchNextMatchByCompetition() {
        log.info("update stations");
        stationsService.saveRecords();
    }
}
