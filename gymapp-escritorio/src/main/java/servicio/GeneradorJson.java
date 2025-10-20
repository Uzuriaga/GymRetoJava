package servicio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import modelo.Ejercicio;
import modelo.Historico;
import modelo.Serie;
import modelo.Usuario;
import modelo.Workout;

import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import java.util.Arrays;

public class GeneradorJson {
	
	private static final int BCRYPT_COST = 12;
	
	public static String hashPassword(String plain) {
        return BCrypt.hashpw(plain, BCrypt.gensalt(BCRYPT_COST));
    }
	
    public static void main(String[] args) throws Exception {
        Gson gson = new GsonBuilder().
        		setPrettyPrinting().
        		setDateFormat("yyyy-MM-dd")
                .create();
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        // Usuarios de prueba

        List<Usuario> usuarios = Arrays.asList(
                new Usuario("u1", "Ana García", df.parse("1992-06-14"), "ana@gmail.com", Usuario.TIPO_CLIENTE, 0,null),
                new Usuario("u2", "Luis Pérez", df.parse("1993-03-05"), "luis@gmail.com", Usuario.TIPO_ENTRENADOR, 2,null)
            );
        
        // Contraseñas en claro correspondientes para generar los hasghess
        List<String> plainPasswords = Arrays.asList("12345", "12345");

        // Generar hashes y asignar a cada usuario
        for (int i = 0; i < usuarios.size(); i++) {
            String plain = plainPasswords.get(i);
            String hash = hashPassword(plain);
            usuarios.get(i).setPasswordHash(hash);
        }

        // Workouts de prueba
        List<Workout> workouts = Arrays.asList(
            new Workout("w1", "Full Body", 0, "https://video.com/fullbody"),
            new Workout("w2", "Cardio Express", 1, "https://video.com/cardio")
        );
        
        
        List<Ejercicio> ejercicios = Arrays.asList(
        	    new Ejercicio("ej1", "Sentadillas", "Ejercicio para piernas", Arrays.asList(
        	        new Serie("Serie 1", 15, "https://foto.com/sentadilla1", 30, 60),
        	        new Serie("Serie 2", 12, "https://foto.com/sentadilla2", 30, 60)
        	    )),
        	    new Ejercicio("ej2", "Flexiones", "Ejercicio para pecho", Arrays.asList(
        	        new Serie("Serie 1", 10, "https://foto.com/flexion1", 20, 40)
        	    ))
        	);

        	List<Historico> historico = Arrays.asList(
        	    new Historico("h1", "u1", "w1", "2025-10-15", 1800, 1800, 100),
        	    new Historico("h2", "u1", "w2", "2025-10-16", 1500, 1600, 94)
        	);
    
        	


        try {
            FileWriter writerUsuarios = new FileWriter("usuarios.json");
            gson.toJson(usuarios, writerUsuarios);
            writerUsuarios.close();

            FileWriter writerWorkouts = new FileWriter("workouts.json");
            gson.toJson(workouts, writerWorkouts);
            writerWorkouts.close();
            
            FileWriter writerEjercicios = new FileWriter("ejercicios.json");
        	gson.toJson(ejercicios, writerEjercicios);
        	writerEjercicios.close();

        	FileWriter writerHistorico = new FileWriter("historico.json");
        	gson.toJson(historico, writerHistorico);
        	writerHistorico.close();

            System.out.println("Archivos JSON generados correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
