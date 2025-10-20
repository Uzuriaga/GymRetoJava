package servicio;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {
    //numero de hash
    private static final int COST = 12;

    public static String hash(String plain) {
        return BCrypt.hashpw(plain, BCrypt.gensalt(COST));
    }

    public static boolean verify(String plain, String hashed) {
        if (hashed == null || hashed.isEmpty()) return false;
        return BCrypt.checkpw(plain, hashed);
    }

    // test
    public static void main(String[] args) {
        String plain = "12345";
        String hashed = hash(plain);
        System.out.println("Hash: " + hashed);
        System.out.println("Verificación correcta: " + verify("12345", hashed));
        System.out.println("Verificación incorrecta: " + verify("wrong", hashed));
    }
}
