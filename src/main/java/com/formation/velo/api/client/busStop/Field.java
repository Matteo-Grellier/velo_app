package com.formation.velo.api.client.busStop;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Field {
    private String stop_id;
    private String stop_name;
    private Double[] stop_coordinates;
}