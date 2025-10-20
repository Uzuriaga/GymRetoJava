package modelo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Date;
import com.google.auto.value.AutoValue.Builder;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Usuario {
    public String usuarioId;
    public String nombre;
    public Date fnacimiento;
    public String email;   
    public String tipoUsuario; //
    public int nivel;
    public static final String TIPO_CLIENTE = "CLIENTE";
    public static final String TIPO_ENTRENADOR = "ENTRENADOR";
    public String passwordHash;
    
  


    /*public enum TipoUsuario{
    	CLIENTE,ENTRENADOR
    }
    
    
    

   /*Lombook es una libreria para ahorrarse los getters setter y el constructor
    * Siendo @Data getters setter y tal
    * luego el AllArgs contructror es un constructor con todos los argumentos
    * y buid opara construir el objeto
    */
}

/*public enum TipoUsuario {
    CLIENTE,
    ENTRENADOR
}*/

