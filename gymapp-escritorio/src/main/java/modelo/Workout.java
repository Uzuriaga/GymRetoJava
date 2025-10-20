package modelo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Workout {
	private String workoutId;
	private String nombre;
	private int nivel;
	private String videoUrl;
}
