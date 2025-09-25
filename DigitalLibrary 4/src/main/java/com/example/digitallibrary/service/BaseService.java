package com.example.digitallibrary.service;

import jakarta.persistence.EntityManager;

public abstract class BaseService {
    protected EntityManager entityManager;

    public BaseService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}