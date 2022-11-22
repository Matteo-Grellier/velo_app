package com.formation.velo.repository;

import com.formation.velo.model.Stations;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StationsRepository extends JpaRepository<Stations, Integer>{

    Optional<Stations> findByRecordId(String recordId);
}
