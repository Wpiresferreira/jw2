package me.devw.wallet.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import me.devw.wallet.entity.User;
import me.devw.wallet.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class AuthenticationService {

    @Inject
    private UserRepository repository;

    public User authenticate(
            String username,
            String password) {

        var userOpt = repository.findByUsername(username);

        if (userOpt.isEmpty()) {
            return null;
        }

        User user = userOpt.get();

        boolean valid = BCrypt.checkpw(
                password,
                user.getPassword()
        );

        if (!valid) {
            return null;
        }

        return user;
    }
}