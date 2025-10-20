package modelo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Serie {
    private String nombre;
    private int repeticiones;
    private String fotoUrl;
    private int tiempo;   //segundos es mas facil, si alguno sabe hacerlo desde date que lo haga
    private int descanso; 
}
