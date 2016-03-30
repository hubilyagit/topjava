package ru.javawebinar.topjava.repository.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.repository.UserMealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

/**
 * User: gkisline
 * Date: 26.08.2014
 */

@Repository
public class JpaUserMealRepositoryImpl implements UserMealRepository {

    @PersistenceContext
    public EntityManager entityManager;

    @Override
    @Transactional
    public UserMeal save(UserMeal userMeal, int userId) {
        User ref = entityManager.getReference(User.class, userId);
        userMeal.setUser(ref);
        if(userMeal.isNew()){
            entityManager.persist(userMeal);
        }
        else {
            entityManager.merge(userMeal);
        }
        return userMeal;
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
       return entityManager.createQuery("DELETE FROM UserMeal um WHERE um.id =:id and um.user.id =:userId")
                .setParameter("id",id)
                .setParameter("userId",userId)
                .executeUpdate()!=0;
    }

    @Override
    public UserMeal get(int id, int userId) {
        return entityManager.find(UserMeal.class,id);
    }

    @Override
    public List<UserMeal> getAll(int userId) {
        return entityManager.createQuery("SELECT um FROM UserMeal um WHERE um.user.id =:userId order by um.id desc ",UserMeal.class).setParameter("userId",userId).getResultList();
    }

    @Override
    public List<UserMeal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return null;
    }
}