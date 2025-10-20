package servicio;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.mindrot.jbcrypt.BCrypt;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Servicio encargado de inicializar Firebase y autenticar usuarios.
 
 */
public class Login {

    private static final String SERVICE_ACCOUNT_PATH = "gymKey.json"; // ajusta ruta la he cambiado a mi bd para pruebas
    private static final int FIRESTORE_TIMEOUT_SECONDS = 15;

    public static void init() throws Exception {
        if (FirebaseApp.getApps().isEmpty()) {
            FileInputStream serviceAccount = new FileInputStream(SERVICE_ACCOUNT_PATH);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(com.google.auth.oauth2.GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);
        }
    }

    public static boolean authenticate(String email, String plainPassword) throws Exception {
        if (email == null || plainPassword == null) return false;
        init();
        Firestore db = FirestoreClient.getFirestore();

        Query q = db.collection("usuarios").whereEqualTo("email", email).limit(1);
        ApiFuture<QuerySnapshot> future = q.get();
        QuerySnapshot snapshot = future.get(FIRESTORE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        List<QueryDocumentSnapshot> docs = snapshot.getDocuments();
        if (docs.isEmpty()) return false;

        DocumentSnapshot doc = docs.get(0);
        Object hashObj = doc.get("passwordHash");
        if (!(hashObj instanceof String)) return false;
        String storedHash = (String) hashObj;
        try {
            return BCrypt.checkpw(plainPassword, storedHash);
        } catch (Exception ex) {
            return false;
        }
    }

    public static String getUserNameByEmail(String email) throws Exception {
        if (email == null) return null;
        init();
        Firestore db = FirestoreClient.getFirestore();
        Query q = db.collection("usuarios").whereEqualTo("email", email).limit(1);
        ApiFuture<QuerySnapshot> future = q.get();
        QuerySnapshot snapshot = future.get(FIRESTORE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        List<QueryDocumentSnapshot> docs = snapshot.getDocuments();
        if (docs.isEmpty()) return null;
        DocumentSnapshot doc = docs.get(0);
        Object nombre = doc.get("nombre");
        return nombre != null ? nombre.toString() : null;
    }

    // Map del documento
    public static Map<String, Object> fetchUserMapByEmail(String email) throws Exception {
        if (email == null) return null;
        init();
        Firestore db = FirestoreClient.getFirestore();
        Query q = db.collection("usuarios").whereEqualTo("email", email).limit(1);
        ApiFuture<QuerySnapshot> future = q.get();
        QuerySnapshot snapshot = future.get(FIRESTORE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        List<QueryDocumentSnapshot> docs = snapshot.getDocuments();
        if (docs.isEmpty()) return null;
        return docs.get(0).getData();
    }
}
