package ma.ensias.employe.service;
import ma.ensias.employe.entite.Employe;
import ma.ensias.employe.repository.EmployeRepo;
import ma.ensias.employe.response.EmployeResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

@Service
public class EmployeService {
    @Autowired
    private EmployeRepo employeRepo;

    @Autowired
    private ModelMapper mapper;

    // VULNÉRABILITÉ 1 (HIGH) : Mot de passe en dur dans le code (Hardcoded Credential)
    private static final String DB_PASSWORD = "SecretPassword123!";
    private static final String DB_USER = "root";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/employedemo";

    public EmployeResponse getEmployeById(int id) {
        Optional<Employe> employe = employeRepo.findById(id);

        // VULNÉRABILITÉ 2 (HIGH) : Injection SQL
        executeUnsafeQuery(String.valueOf(id));

        // VULNÉRABILITÉ 3 (CRITICAL) : Path Traversal; Lecture d'un fichier système arbitraire
        readUserFile("/../../etc/passwd");

        // VULNÉRABILITÉ 4 (CRITICAL) : Cryptographie faible; MD5 et DES
        weakCryptography("donnees_sensibles");

        return mapper.map(employe.get(), EmployeResponse.class);
    }

    // MÉTHODE VULNÉRABLE : Injection SQL
    private void executeUnsafeQuery(String userInput) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            // Concaténation directe = faille d'injection SQL
            String query = "SELECT * FROM employe WHERE id = " + userInput;
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                System.out.println("User: " + rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // MÉTHODE VULNÉRABLE : Path Traversal
    private void readUserFile(String filePath) {
        try {
            // Aucune validation du chemin = lecture de fichier arbitraire
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
        } catch (Exception e) {
            System.err.println("Erreur de lecture du fichier");
        }
    }

    // MÉTHODE VULNÉRABLE : Cryptographie faible (MD5 + DES)
    private void weakCryptography(String input) {
        try {
            // Hachage faible MD5 (obsolète)
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());

            // Chiffrement symétrique obsolète DES (clé en dur)
            byte[] keyBytes = "12345678".getBytes();
            SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cipherText = cipher.doFinal(input.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // MÉTHODE VULNÉRABLE : Désérialisation non sécurisée
    public Object deserializeData(byte[] data) {
        try {
            // ⚠️ Désérialisation d'objets non fiables = RCE possible
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }
}
