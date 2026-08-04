package me.devw.wallet.config;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.devw.wallet.entity.User;
import me.devw.wallet.repository.UserRepository;
import me.devw.wallet.entity.Role;
import me.devw.wallet.security.PasswordService;

import java.time.LocalDateTime;
import java.util.UUID;

@Startup
@Singleton
public class StartupBean {

    @Inject
    private UserRepository repository;
    @Inject
    private PasswordService passwordService;


    @PostConstruct
    @Transactional
    public void init() {

        var existingUser = repository.findByEmail("admin@admin.com");

        if (existingUser.isEmpty()) {

            User user = new User();

            user.setId(UUID.randomUUID());
            user.setName("admin");
            user.setEmail("admin@admin.com");
            user.setPassword(passwordService.hash("123456"));
            user.setRole(Role.ADMIN);
            user.setCreatedAt(LocalDateTime.now());

            repository.save(user);

            System.out.println("User admin created");

        } else {

            User user = existingUser.get();

            user.setPassword(passwordService.hash("123456"));
            user.setRole(Role.ADMIN);

            repository.update(user);

            System.out.println("User admin updated");
        }
    }
}