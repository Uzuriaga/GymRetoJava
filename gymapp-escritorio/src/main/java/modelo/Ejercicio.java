package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class Ejercicio {
    private String ejercicioId;
    private String nombre;
    private String descripcion;
    private List<Serie> series;
}
