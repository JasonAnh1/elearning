package com.jason.elearning.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class BaseRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void flush() {
        entityManager.flush();
    }

    public void detach(Object obj) {
        entityManager.detach(obj);
    }

    public JPAQueryFactory query() {
        return new JPAQueryFactory(entityManager);
    }

}
