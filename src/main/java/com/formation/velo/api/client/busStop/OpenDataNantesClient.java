package com.formation.velo.api.client.busStop;

import com.formation.velo.api.client.busStop.OpenData;

import retrofit2.Call;
import retrofit2.http.GET;
public interface OpenDataNantesClient {
    
    @GET("api/records/1.0/search/?dataset=244400404_tan-arrets&q=&rows=100")
    Call<OpenData> getRecords();
}
