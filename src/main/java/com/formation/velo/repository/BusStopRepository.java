package com.formation.velo.repository;

import com.formation.velo.model.BusStop;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BusStopRepository extends JpaRepository<BusStop, Integer>{
    Optional<BusStop> findByRecordId(String recordId);
}
