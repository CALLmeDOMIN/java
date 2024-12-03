package com.example.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.example.model.Animal;
import com.example.model.AnimalCondition;

public class AnimalDaoImpl implements AnimalDao {
    private final SessionFactory sessionFactory;

    public AnimalDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Animal animal) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            session.persist(animal);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Animal animal) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            session.merge(animal);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Animal animal) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            session.remove(animal);
            session.getTransaction().commit();
        }
    }

    @Override
    public Animal findById(Long id) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            Animal animal = session.get(Animal.class, id);
            session.getTransaction().commit();
            return animal;
        }
    }

    @Override
    public List<Animal> findAll() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            List<Animal> animals = session.createQuery("from Animal", Animal.class).list();
            session.getTransaction().commit();
            return animals;
        }
    }

    @Override
    public List<Animal> findByName(String name) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            Query<Animal> query = session.createQuery("from Animal where name like :name", Animal.class);
            query.setParameter("name", "%" + name + "%");
            List<Animal> animals = query.list();
            session.getTransaction().commit();
            return animals;
        }
    }

    @Override
    public List<Animal> findBySpecies(String species) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            Query<Animal> query = session.createQuery("from Animal where species like :species", Animal.class);
            query.setParameter("species", "%" + species + "%");
            List<Animal> animals = query.list();
            session.getTransaction().commit();
            return animals;
        }
    }

    @Override
    public List<Animal> findByCondition(AnimalCondition condition) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            Query<Animal> query = session.createQuery("from Animal where condition = :condition", Animal.class);
            query.setParameter("condition", condition);
            List<Animal> animals = query.list();
            session.getTransaction().commit();
            return animals;
        }
    }
}
