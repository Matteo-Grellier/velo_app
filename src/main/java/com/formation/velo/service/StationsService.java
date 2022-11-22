package com.formation.velo.service;

import com.formation.velo.api.OpenData;
import com.formation.velo.model.Stations;

import java.util.List;
import java.util.Optional;

public interface StationsService {
    
    List<Stations> findAll();
    Optional<Stations> findById(Integer id);
    Stations save(Stations stations);

    void deleteById(Integer id);

    void delete(Stations stations);

    void saveRecords();

    Optional<Stations> findByRecordId(String recordId);
}
