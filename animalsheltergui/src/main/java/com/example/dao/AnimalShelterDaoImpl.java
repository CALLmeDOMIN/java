package com.example.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.example.model.AnimalShelter;

public class AnimalShelterDaoImpl implements AnimalShelterDao {
    private final SessionFactory sessionFactory;

    public AnimalShelterDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(AnimalShelter animalShelter) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            session.persist(animalShelter);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(AnimalShelter animalShelter) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            session.merge(animalShelter);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(AnimalShelter animalShelter) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            session.remove(animalShelter);
            session.getTransaction().commit();
        }
    }

    @Override
    public AnimalShelter findById(Long id) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            AnimalShelter animalShelter = session.get(AnimalShelter.class, id);
            session.getTransaction().commit();
            return animalShelter;
        }
    }

    @Override
    public List<AnimalShelter> findAll() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            List<AnimalShelter> animalShelters = session.createQuery("from AnimalShelter", AnimalShelter.class).list();
            session.getTransaction().commit();
            return animalShelters;
        }
    }

    @Override
    public List<AnimalShelter> findByName(String name) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            Query<AnimalShelter> query = session.createQuery("from AnimalShelter where name like :name",
                    AnimalShelter.class);
            query.setParameter("name", "%" + name + "%");
            List<AnimalShelter> animalShelters = query.list();
            session.getTransaction().commit();
            return animalShelters;
        }
    }

    @Override
    public void deleteAll() {
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.createMutationQuery("DELETE FROM AnimalShelter").executeUpdate();
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }
}
