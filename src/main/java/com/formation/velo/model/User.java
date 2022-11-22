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

@Table(name = "users")
public class User implements Serializable {

	private static final long serialVersionUID = -767070904974486420L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotBlank(message = "name is mandatory")
	private String name;
	@NotBlank(message = "Surname is mandatory")
	private String surname;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(surname, user.surname);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, surname);
	}
}
