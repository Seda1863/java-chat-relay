package com.example.digitallibrary.model.repository;

import jakarta.persistence.EntityManager;
import com.example.digitallibrary.model.entities.User;

public class UserRepository extends AbstractRepository<User, Long> {

    public UserRepository(EntityManager entityManager) {
        super(entityManager, User.class);
    }

    public User findByUsername(String username) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResult();
    }
}
