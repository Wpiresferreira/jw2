package me.devw.wallet.repository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import me.devw.wallet.entity.User;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserRepository {

    @PersistenceContext(unitName = "securityPU")
    private EntityManager em;

    public void save(User user) {

        em.persist(user);

    }

    public void update(User user) {
        em.merge(user);
    }

    public Optional<User> findByEmail(String email) {

        try {

            User user = em.createQuery(
                            "SELECT u FROM User u WHERE u.email = :email",
                            User.class
                    )
                    .setParameter("email", email)
                    .getSingleResult();

            return Optional.of(user);

        } catch (NoResultException e) {

            return Optional.empty();

        }
    }

    public Optional<User> findById(UUID id) {

        User user = em.find(User.class, id);

        return Optional.ofNullable(user);

    }
}

