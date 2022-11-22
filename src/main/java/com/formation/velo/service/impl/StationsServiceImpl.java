package com.formation.velo.service.impl;

import com.formation.velo.api.OpenData;
import com.formation.velo.api.client.OpenDataNantesClient;
import com.formation.velo.model.Stations;
import com.formation.velo.service.StationsService;

import lombok.extern.java.Log;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.formation.velo.repository.StationsRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
@Log
public class StationsServiceImpl implements StationsService {
    
    public final StationsRepository stationsRepository;

    public StationsServiceImpl(StationsRepository repository) {
        this.stationsRepository = repository;
    }
    
    @Override
    public List<Stations> findAll() {
        return stationsRepository.findAll();
    }

    @Override
    public Optional<Stations> findById(Integer id) {
        return stationsRepository.findById(id);
    }

    @Override
    public Stations save(Stations stations) {
        return stationsRepository.save(stations);
    }

    @Override
    public void deleteById(Integer id) {
        stationsRepository.deleteById(id);
    }

    @Override
    public void delete(Stations stations) {
        stationsRepository.delete(stations);
    }

    @Override
    public void saveRecords() {
        // 1: call OpenData
        String baseUrl = "https://data.nantesmetropole.fr";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();

        OpenDataNantesClient client = retrofit.create(OpenDataNantesClient.class);
        Call<OpenData> openDataVeloNantesCall = client.getRecords();
        try {
            OpenData openDataVeloNantes = openDataVeloNantesCall.execute().body();
            log.info(openDataVeloNantes.toString());
            // 2: Save data in stations (table)

            Arrays.stream(openDataVeloNantes.getRecords()).forEach(record -> {
                Optional<Stations> stations = findByRecordId(record.getRecordId());
                if(stations.isPresent()) {
                    //update station
                    stations.get().setAvailableBikes(record.getField().getAvailableBikes());
                    stations.get().setAvailableBikeStands(record.getField().getAvailableBikeStands());
                    stations.get().setBikeStands(record.getField().getBikeStands());
                    stations.get().setLongitude(record.getField().getPosition()[1]);
                    stations.get().setLattitude(record.getField().getPosition()[0]);
                    stations.get().setStatus(record.getField().getStatus());
                    //save station
                    save(stations.get());
                } else {
                    //create station
                    Stations newStation = Stations.builder()
                        .recordId(record.getRecordId())
                        .name(record.getField().getName())
                        .status(record.getField().getStatus())
                        .bikeStands(record.getField().getBikeStands())
                        .availableBikeStands(record.getField().getAvailableBikeStands())
                        .availableBikes(record.getField().getAvailableBikes())
                        .lattitude(record.getField().getPosition()[0])
                        .longitude(record.getField().getPosition()[1])
                        .address(record.getField().getAddress())
                        .build();
                    //save station
                    save(newStation);
                }
            });

        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Stations> findByRecordId(String recordId) {
        return stationsRepository.findByRecordId(recordId);   
    }
}
