package com.formation.velo.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity

@Table(name = "busStop")
public class BusStop implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String recordId;
	private String stop_name;
  private String stop_id;
  private Double latitude;
  private Double longitude;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusStop busStop = (BusStop) o;
        return Objects.equals(stop_id, busStop.stop_id) && Objects.equals(stop_name, busStop.stop_name) && Objects.equals(latitude, busStop.latitude) && Objects.equals(longitude, busStop.longitude) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stop_id, stop_name, latitude, longitude);
    }
}
