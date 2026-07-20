package me.devw.wallet.security;

import jakarta.enterprise.context.ApplicationScoped;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class PasswordService {

    private static final int COST = 12;

    public String hash(String plainPassword) {
        return BCrypt.hashpw(
                plainPassword,
                BCrypt.gensalt(COST)
        );
    }

    public boolean matches(
            String plainPassword,
            String hashedPassword) {

        return BCrypt.checkpw(
                plainPassword,
                hashedPassword
        );
    }
}