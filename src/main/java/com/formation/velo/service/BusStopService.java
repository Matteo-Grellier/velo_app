package com.formation.velo.service;

import com.formation.velo.model.BusStop;

import java.util.List;
import java.util.Optional;

public interface BusStopService {
    
    List<BusStop> findAll();
    Optional<BusStop> findById(Integer id);
    BusStop save(BusStop BusStop);

    void deleteById(Integer id);

    void delete(BusStop BusStop);

    void saveRecords();

    Optional<BusStop> findByRecordId(String recordId);
}
