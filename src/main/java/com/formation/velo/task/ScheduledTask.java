package com.formation.velo.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.formation.velo.service.BusStopService;
import com.formation.velo.service.StationsService;

import lombok.extern.java.Log;

@Log
@Component
public class ScheduledTask {
    private final StationsService stationsService;
    private final BusStopService busStopService;

    public ScheduledTask(StationsService stationsService, BusStopService busStopService) {
        this.stationsService = stationsService;
        this.busStopService = busStopService;
    }

    @Scheduled(fixedRate = 60000)
    public void searchNextMatchByCompetition() {
        log.info("update stations");
        stationsService.saveRecords();
        log.info("update busStop");
        busStopService.saveRecords();
    }
}
