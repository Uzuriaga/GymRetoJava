package modelo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Historico {
    private String historicoId;
    private String usuarioId;
    private String workoutId;
    private String fecha; // formato ISO: "2025-10-15"
    private int tiempoTotal;
    private int tiempoPrevisto;
    private int porcentajeCompletado;
}
