package me.devw.wallet.service;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import me.devw.wallet.entity.User;

@ApplicationScoped
public class UserService {

    @PersistenceContext
    private EntityManager em;

    public boolean existsByUsername(String username) {
        Long count = em.createQuery(
                        "SELECT COUNT(u) FROM User u WHERE u.username = :username",
                        Long.class
                )
                .setParameter("username", username)
                .getSingleResult();

        return count > 0;
    }

    @Transactional
    public void save(User user) {
        em.persist(user);
    }
}