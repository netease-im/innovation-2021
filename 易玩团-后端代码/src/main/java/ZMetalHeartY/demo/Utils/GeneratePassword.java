package ZMetalHeartY.demo.Utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class GeneratePassword {
    public static String generatePassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}
