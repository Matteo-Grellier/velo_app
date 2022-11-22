package com.formation.velo.service.impl;

import com.formation.velo.api.client.busStop.OpenData;
import com.formation.velo.api.client.busStop.OpenDataNantesClient;
import com.formation.velo.model.BusStop;
import com.formation.velo.service.BusStopService;

import lombok.extern.java.Log;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.formation.velo.repository.BusStopRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
@Log
public class BusStopServiceImpl implements BusStopService {
    
    public final BusStopRepository busStopRepository;

    public BusStopServiceImpl(BusStopRepository repository) {
        this.busStopRepository = repository;
    }
    
    @Override
    public List<BusStop> findAll() {
        return busStopRepository.findAll();
    }

    @Override
    public Optional<BusStop> findById(Integer id) {
        return busStopRepository.findById(id);
    }

    @Override
    public BusStop save(BusStop busStop) {
        return busStopRepository.save(busStop);
    }

    @Override
    public void deleteById(Integer id) {
      busStopRepository.deleteById(id);
    }

    @Override
    public void delete(BusStop busStop) {
      busStopRepository.delete(busStop);
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
            // 2: Save data in BusStop (table)

            Arrays.stream(openDataVeloNantes.getRecords()).forEach(record -> {
                Optional<BusStop> busStop = findByRecordId(record.getRecordId());
                if(busStop.isPresent()) {
                    //update station
                    busStop.get().setStop_id(record.getField().getStop_id());
                    busStop.get().setStop_name(record.getField().getStop_name());
                    busStop.get().setLatitude(record.getField().getStop_coordinates()[0]);
                    busStop.get().setLongitude(record.getField().getStop_coordinates()[1]);
                    //save station 
                    save(busStop.get());
                } else {
                    //create station
                    BusStop newBusStop = BusStop.builder()
                        .stop_id(record.getRecordId())
                        .stop_name(record.getField().getStop_name())
                        .latitude(record.getField().getStop_coordinates()[0])
                        .longitude(record.getField().getStop_coordinates()[1])
                        .build();
                    //save station
                    save(newBusStop);
                }
            });

        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<BusStop> findByRecordId(String recordId) {
        return busStopRepository.findByRecordId(recordId);   
    }
}
