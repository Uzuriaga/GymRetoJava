package servicio;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.Firestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            // Inicializar Firebase (ruta a tu clave privada JSON)
            FileInputStream serviceAccount = new FileInputStream("gymKey.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
            FirebaseApp.initializeApp(options);
            Firestore db = FirestoreClient.getFirestore();

            Gson gson = new Gson();

            // Función reutilizable para cargar y subir un JSON como lista de mapas
            uploadCollectionFromJson(db, gson, "usuarios.json", "usuarios", "usuarioId");
            uploadCollectionFromJson(db, gson, "workouts.json", "workouts", "workoutId");
            uploadCollectionFromJson(db, gson, "ejercicios.json", "ejercicios", "ejercicioId");
            uploadCollectionFromJson(db, gson, "historico.json", "historico", "historicoId");

            System.out.println("Subida completada.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void uploadCollectionFromJson(Firestore db, Gson gson, String filePath, String collectionName, String idField) {
        try (Reader reader = new FileReader(filePath)) {
            List<Map<String, Object>> items = gson.fromJson(reader, new TypeToken<List<Map<String, Object>>>() {}.getType());

            if (items == null || items.isEmpty()) {
                System.out.println("Warning: " + filePath + " está vacío o no se pudo parsear.");
                return;
            }

            for (Map<String, Object> item : items) {
                String docId = extractIdAsString(item, idField);
                if (docId == null || docId.isEmpty()) {
                    // Si no hay id en el objeto, generamos uno con push-id de Firestore
                    docId = db.collection(collectionName).document().getId();
                }
                // Subida síncrona: set().get() para asegurar que la operación finaliza antes de continuar
                db.collection(collectionName)
                  .document(docId)
                  .set(item)
                  .get();
                System.out.println("Subido a " + collectionName + ": " + docId);
            }
        } catch (Exception e) {
            System.out.println("Error subiendo " + filePath + " a " + collectionName);
            e.printStackTrace();
        }
    }

    private static String extractIdAsString(Map<String, Object> item, String idField) {
        Object raw = item.get(idField);
        if (raw == null) return null;
        if (raw instanceof String) return (String) raw;
        if (raw instanceof Number) {
            // Gson suele mapear números a Double por defecto
            return String.valueOf(((Number) raw).longValue());
        }
        return raw.toString();
    }
}
