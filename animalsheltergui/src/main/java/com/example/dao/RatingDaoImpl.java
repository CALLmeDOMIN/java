package com.example.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.example.model.AnimalShelter;
import com.example.model.Rating;

import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class RatingDaoImpl implements RatingDao {
    private final SessionFactory sessionFactory;

    public RatingDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Rating rating) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.persist(rating);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }

    @Override
    public void update(Rating rating) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.merge(rating);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }

    @Override
    public void delete(Rating rating) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.remove(rating);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }

    @Override
    public Rating findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Rating.class, id);
        }
    }

    @Override
    public List<Rating> findAllByShelter(AnimalShelter shelter) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "SELECT r FROM Rating r WHERE r.shelter = :shelter ORDER BY r.ratingDate DESC",
                    Rating.class)
                    .setParameter("shelter", shelter)
                    .getResultList();
        }
    }

    @Override
    public Double getAverageRatingForShelter(AnimalShelter shelter) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Double> query = cb.createQuery(Double.class);
            Root<Rating> root = query.from(Rating.class);

            query.select(cb.avg(root.get("value")))
                    .where(cb.equal(root.get("shelter"), shelter));

            return session.createQuery(query)
                    .getSingleResult();
        } catch (NoResultException e) {
            return 0.0;
        }
    }
}